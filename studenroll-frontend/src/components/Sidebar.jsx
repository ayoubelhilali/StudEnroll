import { GraduationCap, BookOpen, Clock, LogOut } from 'lucide-react';

function Sidebar({ student, activeTab, setActiveTab, enrollmentsCount, handleLogout }) {
  const initials = `${student.firstName?.[0] || ''}${student.lastName?.[0] || ''}`.toUpperCase() || student.cnie.slice(0, 2);

  return (
    <aside className="sidebar">
      <div className="sidebar-top">
        <div className="sidebar-logo">
          <div className="sidebar-logo-icon">
            <GraduationCap size={22} />
          </div>
          <span className="sidebar-logo-text">StudEnroll</span>
        </div>

        <div className="student-card">
          <div className="avatar">{initials}</div>
          <div className="student-info">
            <span className="student-name">{student.firstName} {student.lastName}</span>
            <span className="student-cnie">{student.cnie}</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          <div 
            className={`nav-item ${activeTab === 'courses' ? 'active' : ''}`}
            onClick={() => setActiveTab('courses')}
          >
            <BookOpen size={18} />
            <span>Browse Courses</span>
          </div>
          <div 
            className={`nav-item ${activeTab === 'enrollments' ? 'active' : ''}`}
            onClick={() => setActiveTab('enrollments')}
          >
            <Clock size={18} />
            <span>My Enrollments</span>
            {enrollmentsCount > 0 && (
              <span style={{ 
                marginLeft: 'auto', 
                backgroundColor: 'var(--primary)', 
                color: 'white', 
                fontSize: '11px', 
                fontWeight: 'bold', 
                padding: '2px 8px', 
                borderRadius: '50px' 
              }}>
                {enrollmentsCount}
              </span>
            )}
          </div>
        </nav>
      </div>

      <div className="sidebar-footer">
        <button className="btn-logout" onClick={handleLogout}>
          <LogOut size={16} />
          <span>Sign Out</span>
        </button>
      </div>
    </aside>
  );
}

export default Sidebar;
