"use client";

import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { ActivityEvent, API_URL } from "./api";

export function useLiveActivity(boardId: string | null) {
  const [events, setEvents] = useState<ActivityEvent[]>([]);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    if (!boardId) return;

    const wsUrl = API_URL.replace(/^http/, "ws") + "/ws";
    const client = new Client({
      brokerURL: wsUrl,
      reconnectDelay: 3000,
      onConnect: () => {
        setConnected(true);
        client.subscribe(`/topic/boards/${boardId}`, (message) => {
          const event: ActivityEvent = JSON.parse(message.body);
          setEvents((prev) => [event, ...prev].slice(0, 50));
        });
      },
      onDisconnect: () => setConnected(false),
    });

    client.activate();
    return () => {
      client.deactivate();
    };
  }, [boardId]);

  return { events, connected };
}
