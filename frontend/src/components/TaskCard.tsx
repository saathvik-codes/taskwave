"use client";

import { Task } from "@/lib/api";

const NEXT_STATUS: Record<Task["status"], Task["status"] | null> = {
  TODO: "IN_PROGRESS",
  IN_PROGRESS: "REVIEW",
  REVIEW: "DONE",
  DONE: null,
};

const PRIORITY_COLOR: Record<Task["priority"], string> = {
  LOW: "bg-slate-700 text-slate-200",
  MEDIUM: "bg-blue-700 text-blue-100",
  HIGH: "bg-orange-700 text-orange-100",
  URGENT: "bg-red-700 text-red-100",
};

export function TaskCard({ task, onAdvance }: { task: Task; onAdvance: (id: string, status: Task["status"]) => void }) {
  const next = NEXT_STATUS[task.status];

  return (
    <div className="rounded-lg border border-slate-800 bg-slate-900 p-3 shadow-sm">
      <div className="flex items-start justify-between gap-2">
        <h3 className="text-sm font-medium">{task.title}</h3>
        <span className={`shrink-0 rounded px-2 py-0.5 text-[10px] font-semibold ${PRIORITY_COLOR[task.priority]}`}>
          {task.priority}
        </span>
      </div>
      {task.assignee && <p className="mt-1 text-xs text-slate-400">{task.assignee.fullName}</p>}
      {next && (
        <button
          onClick={() => onAdvance(task.id, next)}
          className="mt-3 rounded bg-slate-800 px-2 py-1 text-xs font-medium text-slate-200 hover:bg-slate-700"
        >
          Move to {next.replace("_", " ")} →
        </button>
      )}
    </div>
  );
}
