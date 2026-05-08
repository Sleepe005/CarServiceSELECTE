ALTER TABLE cars DROP COLUMN fuel_consumption;
ALTER TABLE cars DROP COLUMN maintenance_cost;
ALTER TABLE cars DROP COLUMN capacity_passengers;
ALTER TABLE cars DROP COLUMN capacity_baggage;
ALTER TABLE cars DROP COLUMN acceleration_0_100;
ALTER TABLE cars DROP COLUMN additional_options;
ALTER TABLE cars DROP COLUMN last_updated;


ALTER TABLE cars ADD COLUMN economy_rating BIGINT NOT NULL DEFAULT 0;
ALTER TABLE cars ADD COLUMN capacity_rating BIGINT NOT NULL DEFAULT 0;
ALTER TABLE cars ADD COLUMN dynamics_rating BIGINT NOT NULL DEFAULT 0;
ALTER TABLE cars ADD COLUMN features_rating BIGINT NOT NULL DEFAULT 0;