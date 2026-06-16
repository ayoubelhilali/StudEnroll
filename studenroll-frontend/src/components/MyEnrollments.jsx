import { Calendar, Clock, Lock } from 'lucide-react';

function MyEnrollments({ enrollments, handleCancelEnrollment, setActiveTab }) {
  return (
    <div>
      <div className="view-header">
        <h2 className="view-title">My Enrollments</h2>
        <p className="view-subtitle">Manage and monitor your current course schedule and registrations</p>
      </div>

      {enrollments.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">
            <Calendar size={64} />
          </div>
          <h3 className="empty-title">No Enrolled Courses</h3>
          <p className="empty-desc">You haven't enrolled in any courses for this period yet.</p>
          <button className="btn-secondary" onClick={() => setActiveTab('courses')}>
            Browse Catalog
          </button>
        </div>
      ) : (
        <div className="enrollments-container">
          <div className="table-wrapper">
            <table className="enrollment-table">
              <thead>
                <tr>
                  <th>Enrollment ID</th>
                  <th>Course Title</th>
                  <th>Registration Date</th>
                  <th>Status / Policy</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {enrollments.map(enr => {
                  const dateFormatted = new Date(enr.date).toLocaleDateString(undefined, {
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                  });

                  return (
                    <tr key={enr.enrollmentId}>
                      <td>#{enr.enrollmentId}</td>
                      <td style={{ fontWeight: '600', color: 'var(--text-primary)' }}>{enr.courseName}</td>
                      <td>{dateFormatted}</td>
                      <td>
                        {enr.deletable ? (
                          <span style={{ color: 'var(--success)', fontWeight: '600', fontSize: '13px', display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                            <Clock size={14} /> Cancelable
                          </span>
                        ) : (
                          <span className="badge-locked">
                            <Lock size={12} /> Locked (&gt; 24h)
                          </span>
                        )}
                      </td>
                      <td>
                        {enr.deletable ? (
                          <button 
                            className="btn-cancel"
                            onClick={() => handleCancelEnrollment(enr.enrollmentId, enr.courseName)}
                          >
                            Cancel Enrollment
                          </button>
                        ) : (
                          <span style={{ color: 'var(--text-muted)', fontSize: '13px', fontStyle: 'italic' }}>policy lock</span>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default MyEnrollments;
