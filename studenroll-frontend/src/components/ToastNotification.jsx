import { Check, AlertTriangle } from 'lucide-react';

function ToastNotification({ actionSuccess, actionError }) {
  if (!actionSuccess && !actionError) return null;

  return (
    <div className="toast-container">
      {actionSuccess && (
        <div className="toast success">
          <Check size={20} style={{ flexShrink: 0 }} />
          <div>{actionSuccess}</div>
        </div>
      )}
      {actionError && (
        <div className="toast error">
          <AlertTriangle size={20} style={{ flexShrink: 0 }} />
          <div>{actionError}</div>
        </div>
      )}
    </div>
  );
}

export default ToastNotification;
