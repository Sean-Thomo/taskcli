# Task Tracker CLI

A simple Command Line Interface (CLI) application to help you manage your tasks efficiently. This tool allows you to add, update, delete, and list tasks, as well as track their progress using a JSON file to store the data.

## Features

- **Add Tasks:** Create new tasks with a unique identifier.
- **Update Tasks:** Modify existing tasks by updating their descriptions.
- **Delete Tasks:** Remove tasks by their unique ID.
- **Mark Task Status:** Change the status of tasks to `todo`, `in-progress`, or `done`.
- **List Tasks:** Display all tasks or filter them by status (e.g., show only completed tasks).

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Installation

1. **Clone the Repository:**

   ```bash
   git clone git@github.com:Sean-Thomo/taskcli.git
   cd taskcli
   ```

2. **Install packages:**

   ```bash
   mvn install
   ```

3. **Run application:**

   ```bash
   mvn exec:java -Dexec.mainClass="com.taskcli.Main" -Dexec.args="<command> '<args>'"

   #Example
   #mvn exec:java -Dexec.mainClass="com.taskcli.Main" -Dexec.args="add 'Buy Groceries'"
   ```

### Usage

```bash
 # Adding a new task

 task-cli add "Buy groceries"

 # Output: Task added successfully (ID: 1)

 # Updating and deleting tasks

 task-cli update 1 "Buy groceries and cook dinner"
 task-cli delete 1

 # Marking a task as in progress or done

 task-cli mark-in-progress 1
 task-cli mark-done 1

 # Listing all tasks

 task-cli list

 # Listing tasks by status

 task-cli list done
 task-cli list todo
 task-cli list in-progress
```

### Task Properties

Each task is stored with the following properties:

- `id`: A unique identifier for the task.
- `description`: A short description of the task.
- `status`: The status of the task(`todo`, `in-progress`, `done`).
- `createdAt`: The date and time when the task was created.
- `updatedAt`: The date and time when the task was last updated.
