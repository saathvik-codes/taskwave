"use client";

import { ActivityEvent } from "@/lib/api";

export function ActivityFeed({ events, connected }: { events: ActivityEvent[]; connected: boolean }) {
  return (
    <div className="flex h-full flex-col rounded-lg border border-slate-800 bg-slate-900">
      <div className="flex items-center justify-between border-b border-slate-800 px-3 py-2">
        <h2 className="text-sm font-semibold">Live Activity</h2>
        <span className={`h-2 w-2 rounded-full ${connected ? "bg-green-500" : "bg-slate-600"}`} title={connected ? "Connected" : "Connecting…"} />
      </div>
      <div className="flex-1 space-y-2 overflow-y-auto p-3">
        {events.length === 0 && <p className="text-xs text-slate-500">No activity yet. Create or update a task.</p>}
        {events.map((event) => (
          <div key={event.id} className="rounded border border-slate-800 bg-slate-950 p-2 text-xs">
            <span className="font-mono text-[10px] text-emerald-400">{event.eventType}</span>
            <p className="mt-0.5 text-slate-300">{event.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
