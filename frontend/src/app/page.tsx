"use client";

import { FormEvent, useEffect, useState } from "react";
import { boardActivity, boardTasks, createBoard, createTask, createTeam, Task, updateTaskStatus } from "@/lib/api";
import { useLiveActivity } from "@/lib/useLiveActivity";
import { TaskCard } from "@/components/TaskCard";
import { ActivityFeed } from "@/components/ActivityFeed";

const COLUMNS: Task["status"][] = ["TODO", "IN_PROGRESS", "REVIEW", "DONE"];

export default function HomePage() {
  const [teamId, setTeamId] = useState<string | null>(null);
  const [boardId, setBoardId] = useState<string | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [title, setTitle] = useState("");
  const [priority, setPriority] = useState<Task["priority"]>("MEDIUM");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const { events, connected } = useLiveActivity(boardId);

  useEffect(() => {
    const stored = localStorage.getItem("taskwave_board");
    if (stored) {
      const parsed = JSON.parse(stored);
      setTeamId(parsed.teamId);
      setBoardId(parsed.boardId);
    }
  }, []);

  useEffect(() => {
    if (teamId && boardId) refreshTasks(teamId, boardId);
  }, [teamId, boardId]);

  useEffect(() => {
    if (teamId && boardId && events.length > 0) refreshTasks(teamId, boardId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [events.length]);

  async function refreshTasks(t: string, b: string) {
    try {
      setTasks(await boardTasks(t, b));
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load tasks");
    }
  }

  async function onSetup() {
    setLoading(true);
    setError(null);
    try {
      const team = await createTeam("Demo Team " + Math.floor(Math.random() * 10000));
      const board = await createBoard(team.id, "Sprint Board");
      localStorage.setItem("taskwave_board", JSON.stringify({ teamId: team.id, boardId: board.id }));
      setTeamId(team.id);
      setBoardId(board.id);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Setup failed");
    } finally {
      setLoading(false);
    }
  }

  async function onCreateTask(e: FormEvent) {
    e.preventDefault();
    if (!boardId || !title.trim()) return;
    await createTask(boardId, title.trim(), priority);
    setTitle("");
    if (teamId) refreshTasks(teamId, boardId);
  }

  async function onAdvance(id: string, status: Task["status"]) {
    await updateTaskStatus(id, status);
    if (teamId && boardId) refreshTasks(teamId, boardId);
  }

  if (!boardId) {
    return (
      <main className="flex min-h-screen items-center justify-center p-8">
        <div className="max-w-sm text-center">
          <h1 className="text-2xl font-bold">TaskWave</h1>
          <p className="mt-2 text-sm text-slate-400">
            Spin up a demo team + board against your local backend (port 8150) and watch tasks update live over WebSocket.
          </p>
          <button
            onClick={onSetup}
            disabled={loading}
            className="mt-6 rounded-lg bg-emerald-600 px-5 py-2 font-medium text-white hover:bg-emerald-500 disabled:opacity-50"
          >
            {loading ? "Setting up…" : "Create demo board"}
          </button>
          {error && <p className="mt-3 text-sm text-red-400">{error}</p>}
        </div>
      </main>
    );
  }

  return (
    <main className="grid min-h-screen grid-cols-1 gap-4 p-4 lg:grid-cols-[1fr_320px]">
      <div>
        <div className="mb-4 flex items-center justify-between">
          <h1 className="text-xl font-bold">TaskWave — Sprint Board</h1>
          <form onSubmit={onCreateTask} className="flex gap-2">
            <input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="New task title"
              className="rounded border border-slate-700 bg-slate-900 px-3 py-1.5 text-sm"
            />
            <select
              value={priority}
              onChange={(e) => setPriority(e.target.value as Task["priority"])}
              className="rounded border border-slate-700 bg-slate-900 px-2 py-1.5 text-sm"
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
              <option value="URGENT">Urgent</option>
            </select>
            <button type="submit" className="rounded bg-emerald-600 px-3 py-1.5 text-sm font-medium hover:bg-emerald-500">
              Add
            </button>
          </form>
        </div>

        {error && <p className="mb-3 text-sm text-red-400">{error}</p>}

        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 xl:grid-cols-4">
          {COLUMNS.map((status) => (
            <div key={status} className="rounded-lg bg-slate-900/50 p-2">
              <h2 className="mb-2 px-1 text-xs font-semibold uppercase tracking-wide text-slate-400">
                {status.replace("_", " ")} ({tasks.filter((t) => t.status === status).length})
              </h2>
              <div className="space-y-2">
                {tasks
                  .filter((t) => t.status === status)
                  .map((task) => (
                    <TaskCard key={task.id} task={task} onAdvance={onAdvance} />
                  ))}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="h-[calc(100vh-2rem)]">
        <ActivityFeed events={events} connected={connected} />
      </div>
    </main>
  );
}
