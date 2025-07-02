ALTER TABLE challenge_participants
  DROP CONSTRAINT fkmaxbdxkphx8gcxws0wf9mo2ru;

ALTER TABLE challenge_participants
  ADD CONSTRAINT fk_challenge_participants_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE;
