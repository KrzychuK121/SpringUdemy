DROP TABLE IF EXISTS task_groups;
CREATE TABLE task_groups(
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL,
    done BIT
);
ALTER TABLE task ADD COLUMN task_group_id INT NULL;
ALTER TABLE task ADD FOREIGN KEY (task_group_id) REFERENCES  task_groups (id);