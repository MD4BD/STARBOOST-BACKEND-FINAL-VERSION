
CREATE TABLE notification_read (
    id               BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    notification_id  BIGINT NOT NULL,
    user_id          BIGINT NOT NULL,
    read_at          TIMESTAMP   NOT NULL,

    CONSTRAINT uk_notification_read UNIQUE (notification_id, user_id),
    CONSTRAINT fk_nr_notification FOREIGN KEY (notification_id)
       REFERENCES notification (id)
       ON DELETE CASCADE,
    CONSTRAINT fk_nr_user FOREIGN KEY (user_id)
       REFERENCES users (id)
       ON DELETE CASCADE
);
