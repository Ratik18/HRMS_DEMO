import { AlertCircle, CheckCircle2, Info, LoaderCircle } from 'lucide-react';

export function Alert({ children, type = 'error' }) {
  if (!children) return null;
  const Icon = type === 'success' ? CheckCircle2 : type === 'info' ? Info : AlertCircle;
  return <div className={`alert alert-${type}`} role="status"><Icon size={17} /><span>{children}</span></div>;
}

export function Loading({ label = 'Loading...' }) {
  return <div className="loading-state"><LoaderCircle className="spin" size={20} /><span>{label}</span></div>;
}

export function EmptyState({ title, description, action }) {
  return <div className="empty-state"><div className="empty-icon">⌁</div><h3>{title}</h3><p>{description}</p>{action}</div>;
}
