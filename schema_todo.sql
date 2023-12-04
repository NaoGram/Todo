DROP TABLE IF EXISTS task_groups CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL NOT NULL,
  authority VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS tasks (
  task_id SERIAL NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(20) NOT NULL,
  content VARCHAR(100) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  all_day BOOLEAN,
  status VARCHAR(10) CHECK (status IN ('completed', 'incomplete')) DEFAULT 'incomplete',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  task_group_id INT,
  PRIMARY KEY (task_id)
);

ALTER TABLE tasks ADD CONSTRAINT FK_users_tasks FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS task_groups (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  tasks_id INT NOT NULL,
  group_name VARCHAR(255) NOT NULL,
  color VARCHAR(20),
  PRIMARY KEY (id)
);

ALTER TABLE task_groups ADD CONSTRAINT FK_task_groups_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE task_groups ADD CONSTRAINT FK_task_groups_tasks FOREIGN KEY (tasks_id) REFERENCES tasks;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO todo;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO todo;