import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePage = defineStore("page", () => {
  const page = ref("Platform Launch");

  return { page };
});

export const useModal = defineStore("modal", () => {
  const open = ref(false);
  const openMember = ref(false);
  const openTask = ref(false);
  const openComment = ref(false);
  const openEditBoard = ref(false);
  const openEditTask = ref(false);
  return {
    open,
    openMember,
    openTask,
    openComment,
    openEditBoard,
    openEditTask,
  };
});

export const useBoard = defineStore("board", () => {
  const boards = ref([
    {
      id: "",
      name: "",
      ownerId: "",
    },
  ]);

  const tasks = ref([
    {
      id: "",
      caption: "",
      boardId: "",
      creatorId: "",
      status: "",
    },
  ]);

  const boardMembers = ref([
    {
      boardId: "",
      memberId: "",
      permission: "",
    },
  ]);
  const users = ref([
    {
      id: "",
      name: "",
      username: "",
    },
  ]);

  const user = ref({
    id: "",
    name: "",
    username: "",
  });

  const userId = ref("");

  const isOwner = ref(false);

  const boardId = ref("");
  const title = ref("---");

  const taskId = ref("");

  const selectedBoard = ref({
    id: "",
    name: "",
    ownerId: "",
  });
  const comments = ref([
    {
      id: "",
      comment: "",
      userId: "",
      taskId: "",
    },
  ]);

  const selectedTask = ref({
    id: "",
    caption: "",
    boardId: "",
    creatorId: "",
    status: "",
  });
  const fetchTask = (id: string, name: string) => {
    title.value = name;
    boardId.value = id;
    fetch(`http://localhost:9000/board/task/${id}`, {
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error response");
        }
      })
      .then((data) => {
        tasks.value = data;
        console.debug(data);
      });

    fetch(`http://localhost:9000/users/${id}`, {
      credentials: "include",
    }).then((response) => {
      if (response.ok) {
        isOwner.value = true;
      } else if (response.status === 400) {
        isOwner.value = false;
      } else {
        throw new Error("network error response");
      }
    });

    fetch("http://localhost:9000/board/task/comments/all", {
      credentials: "include",
      method: "GET",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error response");
        }
      })
      .then((data) => {
        comments.value = data;
        console.debug(data);
      });
  };
  const deleteBoard = (id: string) => {
    fetch(`http://localhost:9000/board/delete/${id}`, {
      method: "GET",
      credentials: "include",
    }).then((response) => {
      if (response.ok) {
        return response.ok;
      } else {
        throw new Error("network error response");
      }
    });
  };
  const fetchComments = (id: string) => {
    fetch(`http://localhost:9000/task/comment/${id}`, {
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error response");
        }
      })
      .then((data) => {
        comments.value = data;
        console.debug(data);
      });
  };
  return {
    boards,
    tasks,
    fetchTask,
    boardId,
    title,
    users,
    user,
    isOwner,
    deleteBoard,
    taskId,
    boardMembers,
    selectedTask,
    selectedBoard,
    comments,
    fetchComments,
  };
});
