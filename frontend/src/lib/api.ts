const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8150";

export interface Team {
  id: string;
  name: string;
}

export interface Board {
  id: string;
  name: string;
}

export interface Task {
  id: string;
  title: string;
  description?: string;
  status: "TODO" | "IN_PROGRESS" | "REVIEW" | "DONE";
  priority: "LOW" | "MEDIUM" | "HIGH" | "URGENT";
  assignee?: { fullName: string } | null;
}

export interface ActivityEvent {
  id: string;
  boardId: string;
  taskId: string;
  eventType: string;
  description: string;
  occurredAt: string;
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers },
  });
  if (!res.ok) {
    const body = await res.json().catch(() => ({ message: res.statusText }));
    throw new Error(body.message ?? "Request failed");
  }
  if (res.status === 204) return undefined as T;
  return res.json();
}

export const createTeam = (name: string) =>
  request<Team>("/api/v1/teams", { method: "POST", body: JSON.stringify({ name }) });

export const createBoard = (teamId: string, name: string) =>
  request<Board>(`/api/v1/teams/${teamId}/boards`, { method: "POST", body: JSON.stringify({ name }) });

export const createTask = (boardId: string, title: string, priority: Task["priority"]) =>
  request<Task>(`/api/v1/boards/${boardId}/tasks`, {
    method: "POST",
    body: JSON.stringify({ title, priority }),
  });

export const updateTaskStatus = (taskId: string, status: Task["status"]) =>
  request<Task>(`/api/v1/tasks/${taskId}/status?status=${status}`, { method: "PATCH" });

export const boardTasks = (teamId: string, boardId: string) =>
  request<Task[]>(`/api/v1/teams/${teamId}/boards/${boardId}/tasks`);

export const boardActivity = (teamId: string, boardId: string) =>
  request<ActivityEvent[]>(`/api/v1/teams/${teamId}/boards/${boardId}/activity`);

export { API_URL };
