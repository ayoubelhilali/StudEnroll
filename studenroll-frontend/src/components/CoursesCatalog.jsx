import { Check, Lock, Plus } from 'lucide-react';

function CoursesCatalog({ courses, getCourseEnrollmentCount, isEnrolledInCourse, handleEnroll }) {
  return (
    <div>
      <div className="view-header">
        <h2 className="view-title">Available Courses</h2>
        <p className="view-subtitle">Explore academic courses and click enroll to sign up instantly</p>
      </div>

      <div className="course-grid">
        {courses.map(course => {
          const enrCount = getCourseEnrollmentCount(course.id);
          const isEnrolled = isEnrolledInCourse(course.id);
          const isFull = enrCount >= 3;

          return (
            <div className="course-card" key={course.id}>
              <div>
                <div className="course-meta">
                  <span className="course-badge">{course.credits} Credits</span>
                  <span style={{ 
                    fontSize: '13px', 
                    color: isFull ? 'var(--danger)' : 'var(--success)',
                    fontWeight: '600'
                  }}>
                    {enrCount}/3 Registered
                  </span>
                </div>
                <h3 className="course-card-title">{course.title}</h3>
                <p className="course-card-desc">
                  {course.description || "No description provided for this academic course. Learn core concepts and skills."}
                </p>
              </div>

              <div>
                {isEnrolled ? (
                  <button className="btn-card enrolled" disabled>
                    <Check size={16} />
                    Enrolled
                  </button>
                ) : isFull ? (
                  <button className="btn-card" style={{ backgroundColor: 'rgba(0, 0, 0, 0.05)', color: 'var(--text-muted)', border: '1px solid var(--border-color)', cursor: 'not-allowed' }} disabled>
                    <Lock size={16} />
                    Course Full
                  </button>
                ) : (
                  <button className="btn-card enroll" onClick={() => handleEnroll(course.id, course.title)}>
                    <Plus size={16} />
                    Enroll Now
                  </button>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default CoursesCatalog;
