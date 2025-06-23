'use client';

interface Alert {
  message: string;
  date: string;
  type: string; // warning/info/error
}

interface NotSubmittedUser {
  name: string;
  email: string;
  alert_message: string;
}

interface AlertBoxProps {
  alerts: Alert[];
  notSubmitted?: NotSubmittedUser[];
  isAdmin?: boolean;
}

export default function AlertBox({ alerts, notSubmitted, isAdmin }: AlertBoxProps) {
  return (
    <div className="bg-yellow-100 p-4 rounded mb-4">
      <h3 className="font-semibold mb-2">アラート</h3>
      <ul>
        {alerts.map((alert, i) => (
          <li key={i} className="text-red-700">
            {alert.message}（{alert.date}）
            <span className="ml-2 text-xs text-gray-600">[{alert.type}]</span>
          </li>
        ))}
      </ul>
      {isAdmin && notSubmitted && notSubmitted.length > 0 && (
        <div className="bg-blue-100 p-3 rounded mt-3">
          <h4 className="font-semibold text-blue-700 mb-2">未入力者一覧（{notSubmitted.length}名）</h4>
          <ul className="list-disc list-inside">
            {notSubmitted.map((u, i) => (
              <li key={i} className="text-blue-800">
                {u.name}（{u.email}） - {u.alert_message}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
