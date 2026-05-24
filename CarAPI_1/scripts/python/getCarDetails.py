import urllib.request
import ssl
import re
import json
import time
import random
from datetime import datetime
import sys

ssl._create_default_https_context = ssl._create_unverified_context
sys.stdout.reconfigure(line_buffering=True)

# ============================================
# КОНФИГУРАЦИЯ
# ============================================

DELAY_BETWEEN_REQUESTS = (1, 3)  # задержка от 1 до 3 секунд
MAX_MODELS_PER_BRAND = 50  # максимум моделей на марку (чтобы не слишком долго)

# ============================================
# ФУНКЦИИ ДЛЯ ПАРСИНГА
# ============================================

def fetch_html(url):
    """Загружает HTML страницу"""
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
        'Accept-Language': 'ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3',
    }
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req, timeout=15) as response:
        return response.read().decode('utf-8')

def get_all_brands():
    """Получает список всех марок с главной страницы каталога"""
    url = "https://auto.mail.ru/catalog/"
    html = fetch_html(url)

    # Ищем ссылки на марки
    pattern = r'/catalog/([a-z-]+)/"'
    brands = list(set(re.findall(pattern, html)))

    # Фильтруем дубликаты и странные значения
    brands = [b for b in brands if b and len(b) > 1 and not b.startswith('?')]

    print(f"Найдено марок: {len(brands)}")
    return brands

def get_models_for_brand(brand):
    """Получает список моделей для конкретной марки"""
    url = f"https://auto.mail.ru/catalog/{brand}/"

    try:
        html = fetch_html(url)

        # Ищем ссылки на модели
        pattern = rf'/catalog/{brand}/([^/]+)/"'
        models = list(set(re.findall(pattern, html)))

        # Фильтруем
        models = [m for m in models if m and not m.startswith('?') and len(m) > 1]

        return models[:MAX_MODELS_PER_BRAND]
    except Exception as e:
        print(f"  Ошибка получения моделей для {brand}: {e}")
        return []

def get_generations_for_model(brand, model):
    """Получает поколения для конкретной модели"""
    url = f"https://auto.mail.ru/catalog/{brand}/{model}/"

    try:
        html = fetch_html(url)

        # Ищем годы поколений
        years_pattern = r'<span class="hdr__inner">(\d{4})\s*[–-]\s*(\d{4})</span>'
        years = re.findall(years_pattern, html)

        # Ищем слаги поколений
        slugs_pattern = rf'/catalog/{brand}/{model}/([^/]+)/sedan/"'
        slugs = re.findall(slugs_pattern, html)

        generations = []
        for (year_from, year_to), slug in zip(years, slugs):
            generations.append({
                'year_from': int(year_from),
                'year_to': int(year_to),
                'generation_slug': slug
            })

        return generations
    except Exception as e:
        print(f"   Ошибка получения поколений: {e}")
        return []

def get_car_details(brand, model, generation_slug):
    """Получает технические характеристики для поколения"""
    url = f"https://auto.mail.ru/catalog/{brand}/{model}/{generation_slug}/sedan/"

    details = {
        'power_hp': 0,
        'transmission': 'Automatic',
        'drivetrain': 'FWD',
        'fuel_type': 'Petrol',
        'body_type': 'Sedan',
        'fuel_consumption': None
    }

    try:
        html = fetch_html(url)

        # Мощность
        power_match = re.search(r'Мощность[^>]*>.*?(\d+)\s*л\.с\.', html, re.IGNORECASE)
        if power_match:
            details['power_hp'] = int(power_match.group(1))

        # Расход топлива
        fuel_match = re.search(r'Расход\s*топлива[^>]*>.*?(\d+\.?\d*)\s*л', html, re.IGNORECASE)
        if fuel_match:
            details['fuel_consumption'] = float(fuel_match.group(1))

        # КПП
        trans_match = re.search(r'КПП[^>]*>.*?(Автомат|Механика|Робот|Вариатор)', html, re.IGNORECASE)
        if trans_match:
            trans = trans_match.group(1)
            if trans == 'Автомат':
                details['transmission'] = 'Automatic'
            elif trans == 'Механика':
                details['transmission'] = 'Manual'
            else:
                details['transmission'] = 'Automatic'

        # Привод
        drive_match = re.search(r'Привод[^>]*>.*?(Передний|Задний|Полный)', html, re.IGNORECASE)
        if drive_match:
            drive = drive_match.group(1)
            if drive == 'Передний':
                details['drivetrain'] = 'FWD'
            elif drive == 'Задний':
                details['drivetrain'] = 'RWD'
            elif drive == 'Полный':
                details['drivetrain'] = 'AWD'

        # Тип топлива (по умолчанию)
        if 'гибрид' in html.lower() or 'hybrid' in html.lower():
            details['fuel_type'] = 'Hybrid'
        elif 'электро' in html.lower() or 'electric' in html.lower():
            details['fuel_type'] = 'Electric'

    except Exception as e:
        print(f"    Ошибка получения деталей: {e}")

    return details

# ============================================
# РАСЧЕТ РЕЙТИНГОВ
# ============================================

def safety_rating(year):
    if year >= 2020: return 9
    if year >= 2015: return 7
    if year >= 2010: return 5
    if year >= 2005: return 3
    return 1

def reliability_rating(brand):
    brand = brand.lower()
    if brand in ['toyota', 'lexus']: return 9
    if brand in ['honda', 'hyundai', 'kia']: return 8
    if brand in ['volkswagen', 'bmw', 'mercedes', 'audi']: return 7
    if brand in ['ford', 'nissan', 'mazda', 'subaru']: return 6
    if brand in ['lada', 'renault', 'chevrolet', 'skoda']: return 5
    return 4

def economy_rating(fuel_consumption, fuel_type):
    fuel_type = fuel_type.lower() if fuel_type else 'petrol'
    if fuel_type == 'electric': return 10
    if fuel_type == 'hybrid': return 9
    if fuel_consumption:
        if fuel_consumption <= 5: return 10
        if fuel_consumption <= 7: return 8
        if fuel_consumption <= 9: return 6
        if fuel_consumption <= 11: return 4
        return 2
    return 5

def capacity_rating(body_type):
    body = body_type.lower() if body_type else ''
    if body in ['suv', 'minivan', 'miniven', 'off-road']: return 8
    if body in ['wagon', 'universal']: return 7
    if body in ['sedan', 'limousine']: return 5
    if body in ['hatchback', 'coupe', 'liftback']: return 4
    if body in ['cabriolet', 'roadster']: return 2
    return 5

def dynamics_rating(power_hp):
    if power_hp >= 250: return 10
    if power_hp >= 200: return 8
    if power_hp >= 150: return 6
    if power_hp >= 100: return 4
    if power_hp > 0: return 2
    return 5

def appearance_rating(year, body_type):
    if year >= 2020: base = 8
    elif year >= 2015: base = 7
    elif year >= 2010: base = 6
    else: base = 5

    if body_type and body_type.lower() in ['coupe', 'suv', 'cabriolet']:
        base = min(10, base + 1)
    return base

def calculate_all_ratings(year, brand, power_hp, fuel_consumption, fuel_type, body_type):
    return {
        'safety': safety_rating(year),
        'reliability': reliability_rating(brand),
        'economy': economy_rating(fuel_consumption, fuel_type),
        'comfort': 5,
        'capacity': capacity_rating(body_type),
        'dynamics': dynamics_rating(power_hp),
        'appearance': appearance_rating(year, body_type),
        'features': 5
    }

# ============================================
# СОХРАНЕНИЕ РЕЗУЛЬТАТОВ
# ============================================

def save_to_txt(all_cars, filename="cars_full_output.txt"):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write("=" * 100 + "\n")
        f.write("РЕЗУЛЬТАТЫ ПАРСИНГА ВСЕХ АВТОМОБИЛЕЙ\n")
        f.write(f"Создано: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"Всего записей: {len(all_cars)}\n")
        f.write("=" * 100 + "\n\n")

        for car in all_cars:
            f.write(f"Марка: {car['brand']}\n")
            f.write(f"Модель: {car['model']}\n")
            f.write(f"Поколение: {car['generation']}\n")
            f.write(f"Годы: {car['year_from']} - {car['year_to']}\n")
            f.write(f"Кузов: {car['body_type']}\n")
            f.write(f"КПП: {car['transmission']}\n")
            f.write(f"Привод: {car['drivetrain']}\n")
            f.write(f"Мощность: {car['power_hp']} л.с.\n")
            f.write(f"Топливо: {car['fuel_type']}\n")
            if car.get('fuel_consumption'):
                f.write(f"Расход: {car['fuel_consumption']} л/100км\n")
            f.write("\n--- РЕЙТИНГИ (1-10) ---\n")
            f.write(f"Безопасность: {car['ratings']['safety']}\n")
            f.write(f"Надежность: {car['ratings']['reliability']}\n")
            f.write(f"Экономичность: {car['ratings']['economy']}\n")
            f.write(f"Комфорт: {car['ratings']['comfort']}\n")
            f.write(f"Вместительность: {car['ratings']['capacity']}\n")
            f.write(f"Динамика: {car['ratings']['dynamics']}\n")
            f.write(f"Внешний вид: {car['ratings']['appearance']}\n")
            f.write(f"Доп опции: {car['ratings']['features']}\n")
            f.write("\n" + "-" * 80 + "\n\n")

    print(f"\n Результаты сохранены в {filename}")

def save_to_json(all_cars, filename="cars_full_output.json"):
    with open(filename, 'w', encoding='utf-8') as f:
        json.dump(all_cars, f, ensure_ascii=False, indent=2)
    print(f" JSON сохранен в {filename}")

# ============================================
# ОСНОВНАЯ ФУНКЦИЯ
# ============================================

def main():
    print("=" * 60)
    print(" ЗАПУСК ПОЛНОГО ПАРСИНГА ВСЕХ АВТОМОБИЛЕЙ")
    print("=" * 60)

    # 1. Получаем все марки
    print("\n Шаг 1: Получение списка всех марок...")
    brands = get_all_brands()

    all_cars = []
    total_skipped = 0

    # 2. Для каждой марки
    for brand_idx, brand in enumerate(brands):
        print(f"\n [{brand_idx + 1}/{len(brands)}] Обработка марки: {brand.upper()}")

        # Получаем модели
        models = get_models_for_brand(brand)
        print(f"   Найдено моделей: {len(models)}")

        # 3. Для каждой модели
        for model_idx, model in enumerate(models):
            print(f"\n    [{model_idx + 1}/{len(models)}] Модель: {model}")

            # Получаем поколения
            generations = get_generations_for_model(brand, model)
            print(f"      Поколений: {len(generations)}")

            # 4. Для каждого поколения
            for gen in generations:
                # Получаем детали
                details = get_car_details(brand, model, gen['generation_slug'])

                # Рассчитываем рейтинги
                ratings = calculate_all_ratings(
                    year=gen['year_from'],
                    brand=brand,
                    power_hp=details['power_hp'],
                    fuel_consumption=details.get('fuel_consumption'),
                    fuel_type=details['fuel_type'],
                    body_type=details['body_type']
                )

                # Формируем запись
                car_record = {
                    'brand': brand.capitalize(),
                    'model': model.capitalize(),
                    'generation': gen['generation_slug'],
                    'year_from': gen['year_from'],
                    'year_to': gen['year_to'],
                    'body_type': details['body_type'],
                    'fuel_type': details['fuel_type'],
                    'transmission': details['transmission'],
                    'drivetrain': details['drivetrain'],
                    'power_hp': details['power_hp'],
                    'fuel_consumption': details.get('fuel_consumption'),
                    'ratings': ratings
                }

                all_cars.append(car_record)

                print(f"         {gen['year_from']}-{gen['year_to']} | {details['power_hp']} л.с. | рейтинг {ratings['dynamics']}")

                # Задержка между запросами
                time.sleep(random.uniform(*DELAY_BETWEEN_REQUESTS))

            total_skipped += len(generations)

    # 5. Сохраняем результаты
    print("\n" + "=" * 60)
    print(" СОХРАНЕНИЕ РЕЗУЛЬТАТОВ")
    print("=" * 60)

    save_to_txt(all_cars)
    save_to_json(all_cars)

    # ИТОГИ
    print("\n" + "=" * 60)
    print(" ИТОГИ ПАРСИНГА")
    print("=" * 60)
    print(f" Обработано марок: {len(brands)}")
    print(f" Всего записей: {len(all_cars)}")
    print("=" * 60)

if __name__ == "__main__":
    main()