import { useState, useEffect } from 'react';
import axios from 'axios';
import { Loader2 } from 'lucide-react';

// Subcomponents
import LoginScreen from './components/LoginScreen';
import Sidebar from './components/Sidebar';
import CoursesCatalog from './components/CoursesCatalog';
import MyEnrollments from './components/MyEnrollments';
import ToastNotification from './components/ToastNotification';
import ConfirmModal from './components/ConfirmModal';

const API_BASE = 'http://localhost:8060/api';

function App() {
  // Authentication & Core State
  const [student, setStudent] = useState(() => {
    const savedStudent = localStorage.getItem('student');
    if (savedStudent) {
      try {
        return JSON.parse(savedStudent);
      } catch {
        localStorage.removeItem('student');
      }
    }
    return null;
  });
  const [loginCnie, setLoginCnie] = useState('');
  const [loginError, setLoginError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  // Navigation State
  const [activeTab, setActiveTab] = useState('courses'); // 'courses' or 'enrollments'

  // App Data State
  const [courses, setCourses] = useState([]);
  const [enrollments, setEnrollments] = useState([]);
  const [allEnrollments, setAllEnrollments] = useState([]); // Used for capacity calculations
  const [loadingData, setLoadingData] = useState(false);
  const [actionError, setActionError] = useState(null);
  const [actionSuccess, setActionSuccess] = useState(null);
  const [pendingCancellation, setPendingCancellation] = useState(null);

  const fetchPortalData = async () => {
    setLoadingData(true);
    setActionError(null);
    try {
      // Fetch courses and all enrollments in parallel
      const [coursesRes, enrollmentsRes] = await Promise.all([
        axios.get(`${API_BASE}/courses`),
        axios.get(`${API_BASE}/enrollments`)
      ]);

      setCourses(coursesRes.data);
      setAllEnrollments(enrollmentsRes.data);

      // Filter enrollments for this specific student (case-insensitive comparison)
      const myEnr = enrollmentsRes.data.filter(
        e => e.studentCnie && e.studentCnie.toUpperCase() === student.cnie.toUpperCase()
      );
      setEnrollments(myEnr);
    } catch (err) {
      console.error("Error fetching portal data:", err);
      setActionError("Failed to load catalog data. Please check if services are running.");
    } finally {
      setLoadingData(false);
    }
  };

  // Fetch all courses & enrollments when student is logged in
  useEffect(() => {
    if (student) {
      // eslint-disable-next-line react-hooks/set-state-in-effect
      fetchPortalData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [student]);

  // Handle Login Submit
  const handleLogin = async (e) => {
    e.preventDefault();
    if (!loginCnie.trim()) return;

    setLoginError(null);
    setIsLoading(true);

    try {
      const formattedCnie = loginCnie.trim().toUpperCase();
      const response = await axios.get(`${API_BASE}/students/cnie/${formattedCnie}`);

      if (response.data) {
        const studentData = response.data;
        setStudent(studentData);
        localStorage.setItem('student', JSON.stringify(studentData));
        setLoginCnie('');
      } else {
        setLoginError("Student account not found for this CNIE.");
      }
    } catch (err) {
      console.error("Login failed:", err);
      if (err.response && err.response.status === 404) {
        setLoginError("Invalid CNIE. Student records do not match this ID.");
      } else {
        setLoginError("Server communication error. Verify the backend services are active.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  // Handle Logout
  const handleLogout = () => {
    localStorage.removeItem('student');
    setStudent(null);
    setCourses([]);
    setEnrollments([]);
    setAllEnrollments([]);
    setActiveTab('courses');
  };

  // Helper: Count enrollments per course ID
  const getCourseEnrollmentCount = (courseId) => {
    const course = courses.find(c => c.id === courseId);
    if (!course) return 0;

    return allEnrollments.filter(
      e => e.courseName && e.courseName.toLowerCase() === course.title.toLowerCase()
    ).length;
  };

  // Helper: Check if student is already enrolled in a course ID
  const isEnrolledInCourse = (courseId) => {
    const course = courses.find(c => c.id === courseId);
    if (!course) return false;

    return enrollments.some(
      e => e.courseName && e.courseName.toLowerCase() === course.title.toLowerCase()
    );
  };

  // Handle Course Enrollment
  const handleEnroll = async (courseId, courseTitle) => {
    setActionError(null);
    setActionSuccess(null);

    try {
      await axios.post(`${API_BASE}/enrollments`, {
        studentCnie: student.cnie,
        courseId: courseId
      });

      showToast(`Successfully enrolled in ${courseTitle}!`, 'success');
      fetchPortalData(); // Refresh counts and catalog
    } catch (err) {
      console.error("Enrollment failed:", err);
      const errMsg = err.response?.data || "Failed to process enrollment request.";
      showToast(errMsg, 'error');
    }
  };

  // Handle Enrollment Cancellation Request
  const handleCancelEnrollment = (enrollmentId, courseName) => {
    setPendingCancellation({ enrollmentId, courseName });
  };

  // Execute actual deletion from backend
  const executeCancellation = async () => {
    if (!pendingCancellation) return;
    const { enrollmentId, courseName } = pendingCancellation;
    setPendingCancellation(null);

    setActionError(null);
    setActionSuccess(null);

    try {
      await axios.delete(`${API_BASE}/enrollments/${enrollmentId}`);
      showToast(`Cancelled enrollment in ${courseName}.`, 'success');
      fetchPortalData(); // Refresh list
    } catch (err) {
      console.error("Cancellation failed:", err);
      const errMsg = err.response?.data || "Could not cancel enrollment.";
      showToast(errMsg, 'error');
    }
  };

  // Custom Toast notification system
  const showToast = (message, type) => {
    if (type === 'success') {
      setActionSuccess(message);
      setTimeout(() => setActionSuccess(null), 4000);
    } else {
      setActionError(message);
      setTimeout(() => setActionError(null), 5000);
    }
  };

  // Render Login Page if not authenticated
  if (!student) {
    return (
      <LoginScreen
        loginCnie={loginCnie}
        setLoginCnie={setLoginCnie}
        loginError={loginError}
        isLoading={isLoading}
        handleLogin={handleLogin}
      />
    );
  }

  // Render Dashboard
  return (
    <div className="dashboard-layout">
      {/* Custom Global Toast Alert */}
      <ToastNotification actionSuccess={actionSuccess} actionError={actionError} />

      {/* Left Sidebar Panel */}
      <Sidebar
        student={student}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        enrollmentsCount={enrollments.length}
        handleLogout={handleLogout}
      />

      {/* Main Content Area */}
      <main className="main-content">
        {loadingData && courses.length === 0 ? (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '80%' }}>
            <Loader2 size={40} className="animate-spin" style={{ animation: 'spin 1s linear infinite', color: 'var(--primary)', marginBottom: '16px' }} />
            <p style={{ color: 'var(--text-secondary)' }}>Syncing student portal...</p>
          </div>
        ) : activeTab === 'courses' ? (
          <CoursesCatalog
            courses={courses}
            getCourseEnrollmentCount={getCourseEnrollmentCount}
            isEnrolledInCourse={isEnrolledInCourse}
            handleEnroll={handleEnroll}
          />
        ) : (
          <MyEnrollments
            enrollments={enrollments}
            handleCancelEnrollment={handleCancelEnrollment}
            setActiveTab={setActiveTab}
          />
        )}
      </main>

      {/* Custom Confirmation Modal */}
      <ConfirmModal
        isOpen={!!pendingCancellation}
        courseName={pendingCancellation?.courseName || ''}
        onConfirm={executeCancellation}
        onCancel={() => setPendingCancellation(null)}
      />
    </div>
  );
}

export default App;