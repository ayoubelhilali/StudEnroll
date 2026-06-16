import { GraduationCap, AlertTriangle, Loader2 } from 'lucide-react';

function LoginScreen({ loginCnie, setLoginCnie, loginError, isLoading, handleLogin }) {
  return (
    <div className="login-wrapper">
      <div className="login-card">
        <div className="login-logo">
          <GraduationCap size={32} />
        </div>
        <div className="login-header">
          <h1 className="login-title">StudEnroll Portal</h1>
          <p className="login-subtitle">Enter your CNIE to access classes and registrations</p>
        </div>
        
        {loginError && (
          <div className="inline-alert error">
            <AlertTriangle size={20} style={{ flexShrink: 0 }} />
            <div>{loginError}</div>
          </div>
        )}

        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label className="form-label" htmlFor="cnie-input">Student CNIE</label>
            <input
              id="cnie-input"
              className="form-input"
              type="text"
              placeholder="e.g. AB123456"
              value={loginCnie}
              onChange={(e) => setLoginCnie(e.target.value)}
              disabled={isLoading}
              required
              autoFocus
            />
          </div>
          
          <button className="btn-primary" type="submit" disabled={isLoading}>
            {isLoading ? (
              <>
                <Loader2 size={18} className="animate-spin" style={{ animation: 'spin 1s linear infinite' }} />
                Verifying account...
              </>
            ) : (
              'Sign In'
            )}
          </button>
        </form>
        
        <style>{`
          @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    </div>
  );
}

export default LoginScreen;
