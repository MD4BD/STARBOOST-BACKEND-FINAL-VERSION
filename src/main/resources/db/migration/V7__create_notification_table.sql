
CREATE TABLE notification (
  id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  message      VARCHAR(255)  NOT NULL,
  target_type  VARCHAR(50)   NOT NULL, 
  target_value VARCHAR(100)  NOT NULL, 
  send_at      TIMESTAMP,            
  sent         BOOLEAN    NOT NULL DEFAULT FALSE,
  created_at   TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
