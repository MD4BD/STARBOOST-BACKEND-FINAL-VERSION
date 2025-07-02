
UPDATE challenge_reward_rules
  SET base_amount = 0.0
  WHERE base_amount IS NULL;

ALTER TABLE challenge_reward_rules
  ALTER COLUMN base_amount SET DEFAULT 0.0;

ALTER TABLE challenge_reward_rules
  ALTER COLUMN base_amount SET NOT NULL;
