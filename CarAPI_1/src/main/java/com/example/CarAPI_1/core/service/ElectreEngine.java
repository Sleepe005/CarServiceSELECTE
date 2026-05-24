package com.example.CarAPI_1.core.service;

import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.core.entity.ProfileEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ElectreEngine {

    // ========================
    // КОНСТАНТЫ ДЛЯ КРИТЕРИЕВ
    // ========================

    // Индексы критериев (для удобства)
    private static final int CRIT_PRICE = 0;           // цена (минимизация)
    private static final int CRIT_SAFETY = 1;          // безопасность (максимизация)
    private static final int CRIT_RELIABILITY = 2;     // надёжность (максимизация)
    private static final int CRIT_ECONOMY = 3;         // экономичность (максимизация)
    private static final int CRIT_COMFORT = 4;         // комфорт (максимизация)
    private static final int CRIT_CAPACITY = 5;        // вместительность (максимизация)
    private static final int CRIT_DYNAMICS = 6;        // динамика (максимизация)
    private static final int CRIT_APPEARANCE = 7;      // внешний вид (максимизация)
    private static final int CRIT_SERVICE_COST = 8;    // стоимость обслуживания (минимизация)
    private static final int CRIT_FEATURES = 9;        // доп. функции (максимизация)

    private static final int CRITERIA_COUNT = 10;

    // Направления критериев: true - максимизация, false - минимизация
    private static final boolean[] CRIT_DIRECTION = {
            false,  // CRIT_PRICE - минимизация
            true,   // CRIT_SAFETY - максимизация
            true,   // CRIT_RELIABILITY - максимизация
            true,   // CRIT_ECONOMY - максимизация
            true,   // CRIT_COMFORT - максимизация
            true,   // CRIT_CAPACITY - максимизация
            true,   // CRIT_DYNAMICS - максимизация
            true,   // CRIT_APPEARANCE - максимизация
            false,  // CRIT_SERVICE_COST - минимизация
            true    // CRIT_FEATURES - максимизация
    };

    // Параметры ELECTRE III (q, p, v) для каждого критерия
    // q - граница безразличия (разница несущественна)
    // p - граница предпочтения (разница существенна)
    // v - вето-граница (разница настолько велика, что veto)
    private static final double[] CRIT_Q = { 100000, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 100000, 1.0 };
    private static final double[] CRIT_P = { 500000, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 500000, 2.0 };
    private static final double[] CRIT_V = { 2000000, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 2000000, 4.0 };

    // Порог отношения предпочтения λ (чем выше, тем строже ранжирование)
    private static final double LAMBDA = 0.5;

    // ========================
    // ОСНОВНОЙ МЕТОД РАНЖИРОВАНИЯ
    // ========================

    public List<CarRanking> rankCars(List<CarEntity> cars, ProfileEntity profile) {
        if (cars == null || cars.isEmpty() || profile == null) {
            return new ArrayList<>();
        }

        int n = cars.size();

        // Получить веса критериев из профиля (1-10) и нормализовать
        double[] weights = getNormalizedWeights(profile);

        // Построить матрицу оценок (альтернативы × критерии)
        double[][] evaluations = buildEvaluationMatrix(cars);

        // Рассчитать индексы согласия для всех пар
        double[][] concordance = calculateConcordance(evaluations, weights);

        // Рассчитать индексы несогласия для всех пар
        double[][] discordance = calculateDiscordance(evaluations);

        // Рассчитать общие отношения предпочтения PR
        double[][] preference = calculatePreferenceRelation(concordance, discordance);

        // Построить матрицу предпочтений S (по порогу λ)
        boolean[][] outranking = buildOutrankingMatrix(preference);

        // Выполнить дистилляцию для получения полного ранжирования
        List<Integer> ranking = distillation(outranking);

        // Сформировать результат
        return buildResult(cars, ranking);
    }

    // ========================
    // НОРМАЛИЗАЦИЯ ВЕСОВ
    // ========================

    private double[] getNormalizedWeights(ProfileEntity profile) {
        double[] rawWeights = new double[CRITERIA_COUNT];

        rawWeights[CRIT_PRICE] = profile.getWeightPrice();
        rawWeights[CRIT_SAFETY] = profile.getWeightSafety();
        rawWeights[CRIT_RELIABILITY] = profile.getWeightReliability();
        rawWeights[CRIT_ECONOMY] = profile.getWeightEconomy();
        rawWeights[CRIT_COMFORT] = profile.getWeightComfort();
        rawWeights[CRIT_CAPACITY] = profile.getWeightCapacity();
        rawWeights[CRIT_DYNAMICS] = profile.getWeightDynamics();
        rawWeights[CRIT_APPEARANCE] = profile.getWeightAppearance();
        rawWeights[CRIT_SERVICE_COST] = profile.getWeightServiceCost();
        rawWeights[CRIT_FEATURES] = profile.getWeightFeatures();

        // Суммируем
        double sum = 0;
        for (double w : rawWeights) {
            sum += w;
        }

        // Нормализуем
        if (sum > 0) {
            for (int i = 0; i < CRITERIA_COUNT; i++) {
                rawWeights[i] = rawWeights[i] / sum;
            }
        }

        return rawWeights;
    }

    // ========================
    // ПОСТРОЕНИЕ МАТРИЦЫ ОЦЕНОК
    // ========================

    private double[][] buildEvaluationMatrix(List<CarEntity> cars) {
        int n = cars.size();
        double[][] matrix = new double[n][CRITERIA_COUNT];

        for (int i = 0; i < n; i++) {
            CarEntity car = cars.get(i);
            matrix[i][CRIT_PRICE] = car.getPrice().doubleValue();
            matrix[i][CRIT_SAFETY] = car.getSafetyRating().doubleValue();
            matrix[i][CRIT_RELIABILITY] = car.getReliabilityRating().doubleValue();
            matrix[i][CRIT_ECONOMY] = car.getEconomyRating().doubleValue();
            matrix[i][CRIT_COMFORT] = car.getComfortRating().doubleValue();
            matrix[i][CRIT_CAPACITY] = car.getCapacityRating().doubleValue();
            matrix[i][CRIT_DYNAMICS] = car.getDynamicsRating().doubleValue();
            matrix[i][CRIT_APPEARANCE] = car.getAppearanceRating().doubleValue();

            // Стоимость обслуживания: если нет поля, используем среднее
            double serviceCost = (car.getPrice() != null) ? car.getPrice() / 100000 : 5;
            matrix[i][CRIT_SERVICE_COST] = serviceCost;

            matrix[i][CRIT_FEATURES] = car.getFeaturesRating().doubleValue();
        }

        return matrix;
    }

    // ========================
    // ИНДЕКС СОГЛАСИЯ
    // ========================

    private double[][] calculateConcordance(double[][] evaluations, double[] weights) {
        int n = evaluations.length;
        double[][] concordance = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (i == k) {
                    concordance[i][k] = 1.0; // Альтернатива равна самой себе
                    continue;
                }

                double sum = 0;
                for (int l = 0; l < CRITERIA_COUNT; l++) {
                    double so = calculateSO(evaluations[i][l], evaluations[k][l], l);
                    sum += weights[l] * so;
                }
                concordance[i][k] = sum; // сумма весов уже = 1, поэтому дополнительное деление не нужно
            }
        }

        return concordance;
    }

    private double calculateSO(double valueI, double valueK, int criterionIndex) {
        double diff;

        if (CRIT_DIRECTION[criterionIndex]) {
            // Максимизация: чем больше, тем лучше
            diff = valueI - valueK;
        } else {
            // Минимизация: чем меньше, тем лучше (меняем местами)
            diff = valueK - valueI;
        }

        double q = CRIT_Q[criterionIndex];
        double p = CRIT_P[criterionIndex];

        if (diff >= p) {
            return 1.0;
        } else if (diff > q && diff < p) {
            return diff / p;
        } else { // diff <= q
            return 0.0;
        }
    }

    // ========================
    // ИНДЕКС НЕСОГЛАСИЯ
    // ========================

    private double[][] calculateDiscordance(double[][] evaluations) {
        int n = evaluations.length;
        double[][] discordance = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (i == k) {
                    discordance[i][k] = 0.0;
                    continue;
                }

                // Берём максимальный индекс несогласия по всем критериям
                double maxNe = 0.0;
                for (int l = 0; l < CRITERIA_COUNT; l++) {
                    double ne = calculateNE(evaluations[i][l], evaluations[k][l], l);
                    if (ne > maxNe) {
                        maxNe = ne;
                    }
                }
                discordance[i][k] = maxNe;
            }
        }

        return discordance;
    }

    private double calculateNE(double valueI, double valueK, int criterionIndex) {
        double diff;

        if (CRIT_DIRECTION[criterionIndex]) {
            // Максимизация: проверяем, насколько K лучше I
            diff = valueK - valueI;
        } else {
            // Минимизация: проверяем, насколько I лучше K
            diff = valueI - valueK;
        }

        double q = CRIT_Q[criterionIndex];
        double v = CRIT_V[criterionIndex];

        if (diff <= q) {
            return 0.0;
        } else if (diff >= v) {
            return 1.0;
        } else { // q < diff < v
            return diff / v;
        }
    }

    // ========================
    // ОБЩЕЕ ОТНОШЕНИЕ ПРЕДПОЧТЕНИЯ PR
    // ========================

    private double[][] calculatePreferenceRelation(double[][] concordance, double[][] discordance) {
        int n = concordance.length;
        double[][] preference = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (i == k) {
                    preference[i][k] = 1.0;
                    continue;
                }

                double c = concordance[i][k];
                double ne = discordance[i][k];

                // ND = (1 - NE) / (1 - C), но нужно избежать деления на 0
                double nd;
                if (c == 1.0 && ne == 0.0) {
                    nd = Double.POSITIVE_INFINITY;
                } else if (c == 1.0) {
                    nd = Double.POSITIVE_INFINITY;
                } else {
                    nd = (1 - ne) / (1 - c);
                }

                // PR = ND * C
                // Если ND бесконечность, PR = бесконечность
                if (Double.isInfinite(nd)) {
                    preference[i][k] = Double.POSITIVE_INFINITY;
                } else {
                    preference[i][k] = nd * c;
                }
            }
        }

        return preference;
    }

    // ========================
    // ПОСТРОЕНИЕ МАТРИЦЫ ПРЕДПОЧТЕНИЙ S
    // ========================

    private boolean[][] buildOutrankingMatrix(double[][] preference) {
        int n = preference.length;
        boolean[][] outranking = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (i == k) {
                    outranking[i][k] = true;
                    continue;
                }

                // PR должен быть >= λ и не бесконечность
                outranking[i][k] = preference[i][k] >= LAMBDA && !Double.isInfinite(preference[i][k]);
            }
        }

        return outranking;
    }

    // ========================
    // ДИСТИЛЛЯЦИЯ (РАНЖИРОВАНИЕ)
    // ========================

    private List<Integer> distillation(boolean[][] outranking) {
        int n = outranking.length;

        // Для каждой альтернативы считаем, сколько других альтернатив её превосходят
        int[] defeatCount = new int[n];

        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int k = 0; k < n; k++) {
                if (i != k && outranking[k][i]) {
                    // k лучше i (k не хуже i, но не равно)
                    // Проверяем, есть ли обратное отношение
                    if (!outranking[i][k]) {
                        count++;
                    }
                }
            }
            defeatCount[i] = count;
        }

        // Сортируем альтернативы по возрастанию defeatCount
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            indices.add(i);
        }

        indices.sort((a, b) -> {
            int cmp = Integer.compare(defeatCount[a], defeatCount[b]);
            if (cmp != 0) return cmp;
            // При равном количестве поражений - сравниваем "победы"
            return Integer.compare(getWinCount(a, outranking), getWinCount(b, outranking));
        });

        return indices;
    }

    private int getWinCount(int index, boolean[][] outranking) {
        int count = 0;
        for (int k = 0; k < outranking.length; k++) {
            if (index != k && outranking[index][k]) {
                count++;
            }
        }
        return count;
    }

    // ========================
    // ФОРМИРОВАНИЕ РЕЗУЛЬТАТА
    // ========================

    private List<CarRanking> buildResult(List<CarEntity> cars, List<Integer> ranking) {
        List<CarRanking> result = new ArrayList<>();
        int rank = 1;

        for (int index : ranking) {
            result.add(new CarRanking(cars.get(index), rank++));
        }

        return result;
    }

    // ========================
    // ВСПОМОГАТЕЛЬНЫЙ КЛАСС ДЛЯ РЕЗУЛЬТАТА
    // ========================

    public static class CarRanking {
        private final CarEntity car;
        private final int rank;

        public CarRanking(CarEntity car, int rank) {
            this.car = car;
            this.rank = rank;
        }

        public CarEntity getCar() { return car; }
        public int getRank() { return rank; }

        public String getBrand() { return car.getBrand(); }
        public String getModel() { return car.getModel(); }
        public Long getPrice() { return car.getPrice(); }
        public Long getSafetyRating() { return car.getSafetyRating(); }
    }
}