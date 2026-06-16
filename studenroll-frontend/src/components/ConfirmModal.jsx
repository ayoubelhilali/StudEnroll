import { AlertTriangle } from 'lucide-react';

function ConfirmModal({ isOpen, courseName, onConfirm, onCancel }) {
  if (!isOpen) return null;

  return (
    <div className="confirm-modal-overlay">
      <div className="confirm-modal-box">
        <div className="confirm-modal-icon">
          <AlertTriangle size={28} />
        </div>
        
        <h3 className="confirm-modal-title">Cancel Enrollment</h3>
        
        <p className="confirm-modal-message">
          Are you sure you want to cancel your enrollment in <strong className="confirm-modal-highlight">"{courseName}"</strong>?
        </p>

        <div className="confirm-modal-actions">
          <button className="confirm-modal-btn cancel" onClick={onCancel}>
            No, Keep Enrollment
          </button>
          <button className="confirm-modal-btn confirm" onClick={onConfirm}>
            Yes, Cancel
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmModal;
