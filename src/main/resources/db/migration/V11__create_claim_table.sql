
CREATE TABLE claim (
  id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  title       VARCHAR(100)  NOT NULL,
  message     TEXT           NOT NULL,
  created_at  TIMESTAMP      NOT NULL,
  sent        BOOLEAN        NOT NULL,
  read        BOOLEAN        NOT NULL,
  user_id     BIGINT         NOT NULL,
  CONSTRAINT fk_claim_user FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
);
