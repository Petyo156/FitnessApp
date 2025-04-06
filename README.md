# Fitness App

The goal of the app is to help fitness enthusiasts manage their workouts and programs, while also functioning as a social network where users can share their programs.



## Key Features ⚙️

### 1. Registration and Login  
- Users can register and log in.

### 2. Navigation  
- Every page includes a navigation bar for easy access.

### 3. Adding Exercises  
- Users can add exercises, which are approved/rejected by admins. Once approved, they are available to everyone.

### 4. Workouts 💥
- Combine exercises into workouts with:
  - **Sets**
  - **Reps**
  - **Added weight**
  
- Workouts are visible only to the user who created them.

### 5. Programs 📅
- Users can create programs with workouts for each day of the week.  
- Programs can be shared with others, and visibility can be controlled.

### 6. Sharing Programs 📢
- Shared programs can be viewed via the **Community** tab or a user's profile.

### 7. Liking Programs ❤️
- Users can like programs, which sends a notification to the owner.

### 8. Logging Workouts 🏃‍♂️
- Users can log their workout results, recording:
  - **Sets**
  - **Reps**
  - **Added weight**

### 9. Administrator Features ⚡
- Admins can:
  - Manage user profiles (disable accounts)
  - Promote users to admins
  - Approve/reject exercises

## Project Structure 📂

### Core Components:
- **Exercises**: Stores exercise details, categories, and admin approval.
- **Workouts**: Combines exercises with specific parameters.
- **Programs**: Links workouts to specific days of the week.
- **User Roles**: Regular users and admins.

## Technologies Used 🛠️:
- **Spring Boot** for backend
- **Thymeleaf** for rendering HTML
- **JavaScript** for dynamic front-end
- **MySQL** for data storage
- **Maven** for dependency management

---

## Основни функционалности - Български 🇧🇬⚙️

### 1. Регистрация и Логин  
- Потребителите могат да се регистрират и влизат.

### 2. Навигация  
- Всяка страница включва навигационен бар за лесно придвижване.

### 3. Добавяне на упражнения  
- Потребителите добавят упражнения, които се одобряват/отхвърлят от администраторите.

### 4. Тренировки 💥
- Комбинират упражнения в тренировки с:
  - **Брой серии**
  - **Брой повторения**
  - **Добавена тежест**
  
- Тренировките се виждат само от създателя.

### 5. Програми 📅
- Потребителите създават програми за всеки ден от седмицата.  
- Програмите могат да се споделят с други потребители.

### 6. Споделяне на програми 📢
- Споделените програми могат да се видят през таба **Community** или в профила на потребителя.

### 7. Харесване на програми ❤️
- Потребителите могат да харесват програми, което води до нотификация на собственика.

### 8. Логване на тренировки 🏃‍♂️
- Потребителите записват резултатите си за тренировки с:
  - **Брой серии**
  - **Брой повторения**
  - **Добавена тежест**

### 9. Администраторски функции ⚡
- Администраторите могат да:
  - Управляват профилите на потребителите
  - Промотират потребители
  - Одобряват/отхвърлят упражнения

## Структура на проекта 📂

### Основни елементи:
- **Упражнения**: Съхраняват се упражнения и категории.
- **Тренировки**: Комбинират упражнения с конкретни параметри.
- **Програми**: Свързват тренировки към дни от седмицата.
- **Потребителски роли**: Обикновени потребители и администратори.

## Технологии, използвани в проекта 🛠️:
- **Spring Boot** за backend логика
- **Thymeleaf** за рендериране на HTML
- **JavaScript** за динамика
- **MySQL** за съхранение на данни
- **Maven** за управление на зависимостите
