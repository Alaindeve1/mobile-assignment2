# Instagram Clone - Student Management System

## 📱 Project Overview
A multi-activity Android application built for AUCA Mobile Programming course. The app demonstrates proper use of Activities, Intents (both Explicit and Implicit), ListViews, and data flow between components.

---

## 🏗️ Activities Used

### 1. **LoginActivity** (Main/Launcher)
- **Purpose:** User authentication entry point
- **Features:**
  - Login form with validation
  - Navigation to Sign Up
  - Direct access to Student List
  - Navigates to Dashboard on successful login

### 2. **SignUpActivity**
- **Purpose:** New user registration
- **Features:**
  - Registration form with multiple input types (EditText, RadioButton, CheckBox)
  - Input validation (empty fields, password matching, email format)
  - Gender selection using RadioGroup
  - Terms & Conditions checkbox
  - Navigates to Dashboard after successful registration

### 3. **DashboardActivity** (Innovation Feature)
- **Purpose:** Central hub for quick access to all features
- **Features:**
  - Display real-time date and time
  - Quick statistics display
  - 6 interactive cards for navigation (Student List, Profile, Emergency, Calendar, Settings, About)
  - Welcome message with username
  - Single logout point

### 4. **StudentListActivity**
- **Purpose:** Display all registered students
- **Features:**
  - ListView showing all students (ID and Name)
  - 8 pre-loaded dummy students
  - Click listener on each list item
  - Navigation back to Login

### 5. **UserDetailsActivity**
- **Purpose:** Display detailed student/user information
- **Features:**
  - Dynamic content based on source (Login, SignUp, or StudentList)
  - Clickable Phone field (Implicit Intent to dialer)
  - Clickable Email field (Implicit Intent to email app)
  - AUCA website link (Implicit Intent to browser)
  - Edit Profile and Logout buttons

---

## 🔄 Intents Used

### **Explicit Intents** (Navigation between app activities)

| From Activity | To Activity | Purpose | Data Passed |
|--------------|-------------|---------|-------------|
| LoginActivity | SignUpActivity | User wants to register | None |
| LoginActivity | DashboardActivity | Successful login | `username` |
| LoginActivity | StudentListActivity | View all students | None |
| SignUpActivity | DashboardActivity | Registration complete | `fullName`, `email`, `gender`, `newsletter`, `source` |
| DashboardActivity | StudentListActivity | Access student list | None |
| DashboardActivity | UserDetailsActivity | View own profile | `username`, `source` |
| StudentListActivity | UserDetailsActivity | View student details | `studentId`, `fullName`, `email`, `phone`, `gender`, `source` |
| Any Activity | LoginActivity | Logout action | Clear all with flags |

### **Implicit Intents** (Launch external apps)

| Action | Intent Type | Purpose | Data Format |
|--------|-------------|---------|-------------|
| Phone Call | `ACTION_DIAL` | Open dialer with number | `tel:+250788123456` |
| Send Email | `ACTION_SENDTO` | Open email app | `mailto:student@auca.ac.rw` |
| Open Website | `ACTION_VIEW` | Open browser | `https://www.auca.ac.rw` |
| Emergency Call | `ACTION_DIAL` | Quick dial emergency | `tel:112` |

---

## 📊 Data Flow Between Activities

### **Flow 1: Login → Dashboard → Student List → Details**
```
LoginActivity
  ├─ Input: username, password
  └─ Output: username (String)
        ↓ [Explicit Intent]
DashboardActivity
  ├─ Receives: username
  ├─ Displays: Welcome message, date/time
  └─ User clicks "Student List" card
        ↓ [Explicit Intent]
StudentListActivity
  ├─ Displays: ArrayList<Student> (8 students)
  ├─ User clicks: Student item at position X
  └─ Output: Student object data
        ↓ [Explicit Intent + putExtra()]
UserDetailsActivity
  ├─ Receives: studentId, fullName, email, phone, gender, source
  ├─ Displays: All student information
  └─ User clicks phone/email
        ↓ [Implicit Intent]
External App (Dialer/Email)
```

### **Flow 2: Sign Up → Dashboard**
```
SignUpActivity
  ├─ Input: fullName, email, password, gender, checkboxes
  ├─ Validation: All fields checked
  └─ Output: Multiple data fields
        ↓ [Explicit Intent + multiple putExtra()]
DashboardActivity
  ├─ Receives: User registration data
  └─ Displays: Welcome message
```

### **Flow 3: Implicit Intent Data Flow**
```
UserDetailsActivity
  ├─ Student phone: +250788123456
  ├─ Student email: student@auca.ac.rw
  └─ User clicks phone TextView
        ↓
Intent Creation
  ├─ Intent(Intent.ACTION_DIAL)
  ├─ setData(Uri.parse("tel:+250788123456"))
  └─ startActivity(dialIntent)
        ↓
System Intent Resolver
  └─ Opens Phone Dialer App (external)
```

---

## 💾 Data Handling

### **Student Class (Data Model)**
```java
public class Student implements Serializable {
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String password;
    
    // Constructor, Getters, Setters
}
```

### **Data Transfer Methods**

#### **1. Intent.putExtra() - Simple Data**
```java
// Sending data
intent.putExtra("username", "john");
intent.putExtra("email", "john@example.com");

// Receiving data
String username = intent.getStringExtra("username");
String email = intent.getStringExtra("email");
```

#### **2. Intent.putExtra() - Multiple Fields**
```java
// In StudentListActivity
intent.putExtra("studentId", student.getStudentId());
intent.putExtra("fullName", student.getName());
intent.putExtra("email", student.getEmail());
intent.putExtra("phone", student.getPhone());
intent.putExtra("gender", student.getGender());
intent.putExtra("source", "studentlist");

// In UserDetailsActivity
String studentId = intent.getStringExtra("studentId");
String fullName = intent.getStringExtra("fullName");
// ... etc
```

#### **3. Source Tracking**
The app uses a "source" string to track where the user came from:
- `"login"` - Came from LoginActivity
- `"signup"` - Came from SignUpActivity  
- `"studentlist"` - Came from StudentListActivity

This allows UserDetailsActivity to display different information based on the source.

---

## 🎯 Key Learning Outcomes

### **Activities & Navigation**
✅ Created 5 distinct activities with clear purposes  
✅ Proper activity lifecycle management  
✅ Appropriate use of `finish()` to manage back stack  
✅ Used `FLAG_ACTIVITY_CLEAR_TOP` for logout  

### **Explicit Intents**
✅ Navigation between app activities  
✅ Passing simple data types (String, int, boolean)  
✅ Passing multiple data fields simultaneously  
✅ Source tracking for conditional rendering  

### **Implicit Intents**
✅ Phone dialer integration (`ACTION_DIAL`)  
✅ Email app integration (`ACTION_SENDTO`)  
✅ Web browser integration (`ACTION_VIEW`)  
✅ Proper error handling for missing apps  

### **ListView Implementation**
✅ ArrayAdapter for simple list display  
✅ OnItemClickListener for item clicks  
✅ Custom data formatting (ID - Name)  

### **Data Management**
✅ Created reusable Student model class  
✅ Proper data encapsulation with getters/setters  
✅ ArrayList for managing multiple objects  
✅ Data validation before transfer  

### **UI/UX Components**
✅ EditText with input types and hints  
✅ RadioGroup and RadioButton for gender selection  
✅ CheckBox for terms acceptance  
✅ ListView with dividers  
✅ CardView for dashboard cards  
✅ Clickable TextViews for phone/email  

---

## 🚀 How to Run

1. Clone or download the project
2. Open in Android Studio
3. Sync Gradle files
4. Build → Clean Project
5. Build → Rebuild Project
6. Run on emulator or physical device

---

## 📝 Testing Checklist

- [x] Login with valid credentials → Dashboard appears
- [x] Sign up with all fields → Dashboard appears
- [x] Click "View All Students" → 8 students displayed
- [x] Click any student → Details displayed correctly
- [x] Click phone number → Dialer opens
- [x] Click email address → Email app opens
- [x] Click AUCA website → Browser opens
- [x] Emergency button → Dial 112
- [x] Logout from any screen → Returns to Login

---

## 👨‍💻 Developer
**Course:** Mobile Programming (INSY 8414)  
**Institution:** Adventist University of Central Africa (AUCA)  
**Assignment:** #2 - Extended with Innovation Features

---

## 📄 License
Educational project for AUCA Mobile Programming course.