DROP TABLE IF EXISTS projects;
CREATE TABLE projects(
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL
);

ALTER TABLE task_groups ADD COLUMN project_id INT NULL;
ALTER TABLE task_groups ADD FOREIGN KEY (project_id) REFERENCES projects (id);

DROP TABLE IF EXISTS project_steps;
CREATE TABLE project_steps(
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL,
    days_to_deadline BIGINT,
    project_id INT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (id)
);
