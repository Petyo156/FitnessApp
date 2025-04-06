# Fitness App

The goal of the application is to help fitness enthusiasts manage their workouts and programs, while also functioning as a social network where users can share their programs.

## Key Features - English ⚙️

### 1. Registration and Login 📝
- Users can register and log into the system.

### 2. Navigation 
- Every page in the app includes a navigation bar for easy navigation.

### 3. Adding Exercises 🏋️‍♀️
- Users can add new exercises, which will be approved or rejected by administrators. Once approved, exercises become available to everyone.

### 4. Workouts 💥
- Exercises can be combined into workouts, which include:
    - **Number of sets** 
    - **Number of reps** 
    - **Added weight** 🏋️‍♂
  
- All workouts created by a user are visible only to that user.

### 5. Programs 📅
- Users can create programs that include workouts for each day of the week.
- Programs can be shared with other users, and it is possible to select whether they should be visible to others.

### 6. Sharing Programs 📢
- Programs shared by other users can be viewed through the **Community** tab in the navigation bar or on the profile of a specific user.

### 7. Liking Programs ❤️
- Through the like microservice, users can like programs, which sends a notification to the owner of the program.

### 8. Logging Workouts 🏃‍♂️
- Users can log their results for a specific workout day by selecting an active program and recording data for:
    - **Number of sets** 
    - **Number of reps** 
    - **Added weight** 

### 9. Administrator Features ⚡
- Administrators can:
    - Manage user profiles (disable accounts) 
    - Promote users to administrators 
    - Approve or reject added exercises 

## Project Structure 📂

### Core Components:
- **Exercises**  
  Classes for storing different exercises with attributes for description, categories, and approval by an administrator.
  
- **Workouts**  
  Classes for combining exercises into workouts, with the ability to set specific parameters for each exercise.

- **Programs**  
  Classes for storing weekly programs, with the ability to link workouts to specific days of the week.

- **User Roles**  
  Users can have different roles: regular users and administrators.

---

## Technologies Used 🛠️:
- **Spring Boot** for backend logic 
- **Thymeleaf** for server-side rendering of HTML pages 
- **JavaScript** for additional front-end dynamic behavior 
- **MySQL** for data storage 
- **Maven** for dependency management 

---

## Основни функционалности - Български ⚙️

### 1. Регистрация и Логин 📝
- Потребителите могат да се регистрират и да влизат в системата.

### 2. Навигация 🧭
- Всяка страница в приложението включва навигационен бар за лесно придвижване.

### 3. Добавяне на упражнения 🏋️‍♀️
- Потребителите могат да добавят нови упражнения, които да бъдат одобрявани или отхвърляни от администраторите. След одобрение, упражненията стават достъпни за всички.

### 4. Тренировки 💥
- Упражненията могат да се комбинират в тренировки, които включват:
    - **Брой серии** 
    - **Брой повторения** 
    - **Добавена тежест** 
  
- Всички тренировки, създадени от потребителя, се виждат само от него.

### 5. Програми 📅
- Потребителите могат да създават програми, които включват тренировки за всеки ден от седмицата. 📆
- Програмите могат да се споделят с други потребители, като може да се избере дали да бъдат видими за другите.

### 6. Споделяне на програми 📢
- Програмите, споделени от други потребители, могат да бъдат разглеждани през таба **Community** в навигационния бар или на профила на конкретен потребител.

### 7. Харесване на програми ❤️
- Чрез микросървиса за лайкове, потребителите могат да харесват програми, което води до изпращане на нотификация на собственика на програмата.

### 8. Логване на тренировки 🏃‍♂️
- Потребителите могат да записват резултатите си за конкретен тренировъчен ден, като се избира активна програма и се записват данни за:
    - **Брой серии** 
    - **Брой повторения** 
    - **Добавена тежест** 

### 9. Администраторски функции ⚡
- Администраторите могат да:
    - Управляват профилите на потребителите (деактивиране на акаунти) 
    - Промотират потребители като администратори
    - Одобряват или отхвърлят добавени упражнения 

---

## Структура на проекта 📂

### Основни елементи:
- **Упражнения**  
  Класове за съхранение на различни упражнения, с атрибути за описание, категории и одобрение от администратор.
  
- **Тренировки**  
  Класове за комбиниране на упражнения в тренировка, с възможност за задаване на конкретни параметри за всяко упражнение.

- **Програми**  
  Класове за съхранение на програми за седмица, с възможност за свързване на тренировки към различни дни от седмицата.

- **Потребителски роли**  
  Потребителите могат да имат различни роли: обикновен потребител и администратор.

---

## Технологии, използвани в проекта 🛠️:
- **Spring Boot** за backend логиката 
- **Thymeleaf** за сървърно рендериране на HTML страниците 
- **JavaScript** за допълнителна динамика на потребителския интерфейс 
- **MySQL** за съхранение на данни 
- **Maven** за управление на зависимостите 

---
