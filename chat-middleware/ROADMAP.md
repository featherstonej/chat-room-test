# Chat Application Roadmap & Development Milestones

> **Project**: `chat-middleware`  
> **Tech Stack**: Java 21, Spring Boot 4, Spring Data JPA, PostgreSQL, Hibernate, Jackson, WebSocket / STOMP  
> **Repository**: [chat-middleware](file:///home/james/Documents/Mentorship/chat-room/chat-middleware)

---

## 1. Project Overview & Progress Tracking

```mermaid
gantt
    title Chat Application Development Timeline
    dateFormat  YYYY-MM-DD
    section Backend Foundation
    Phase 1: JPA Entities & ERD Redesign       :done,    des1, 2026-08-15, 2026-08-20
    Phase 2: Data Access Objects (DAOs)        :done,    des2, 2026-08-21, 2026-08-25
    Phase 3: REST Controllers & DTOs           :done,    des3, 2026-08-26, 2026-09-01
    section Core Middleware
    Phase 4: Service Layer & Business Logic    :active,  des4, 2026-09-02, 2026-09-10
    Phase 5: Security, Auth & JWT Tokens       :         des5, 2026-09-11, 2026-09-18
    Phase 6: Real-Time WebSockets (STOMP)      :         des6, 2026-09-19, 2026-09-27
    section Fullstack Delivery
    Phase 7: Frontend Integration & Testing    :         des7, 2026-09-28, 2026-10-08
```

---

## 2. Detailed Milestone Breakdown

### ✅ Phase 1: JPA Entity Architecture & Schema Design (Completed)
* Standardized 9 core entities mapped to PostgreSQL with foreign key relationships and unique constraints.
* Redesigned message edit auditing and self-referencing reply threads.
* **Documentation**: Detailed in [ENTITY_ANALYSIS.md](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/ENTITY_ANALYSIS.md).

### ✅ Phase 2: Data Access Layer / DAOs (Completed)
Implemented 9 Spring Data JPA repository interfaces with custom queries, pagination, and traversal methods:
* [`UserDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/UserDao.java): Unique username lookup, auth queries, user search.
* [`ConversationDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/ConversationDao.java): Direct chats, group channels, user participation queries.
* [`MessageDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/MessageDao.java): Chronological history, pagination, thread replies, in-chat search.
* [`MessageEditDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/MessageEditDao.java): Full revision history & editor tracking.
* [`GroupDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/GroupDao.java): Group discovery & user group listings.
* [`GroupUserDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/GroupUserDao.java): Role checks (ADMIN/MEMBER), member rosters, leave/kick actions.
* [`GroupConversationDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/GroupConversationDao.java): Group channel associations & permission overrides.
* [`UserConversationDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/UserConversationDao.java): User conversation membership & custom personal settings.
* [`UserFriendDao.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/dao/UserFriendDao.java): Friend requests, active friendships, bidirectional lookups.

### ✅ Phase 3: REST API Controllers & DTO Contracts (Completed)
Implemented REST controllers with CORS support, typed DTOs, and error handling:
* [`UsersController.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/controllers/UsersController.java): Registration, authentication, user profiles, username checks.
* [`ConversationsController.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/controllers/ConversationsController.java): 1-on-1 direct conversations, group channels, participants.
* [`MessagesController.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/controllers/MessagesController.java): Message sending, editing with audit history, thread replies, search.
* [`GroupsController.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/controllers/GroupsController.java): Group creation, member invites, role management, channels.
* [`FriendsController.java`](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/src/main/java/com/example/chat_middleware/controllers/FriendsController.java): Friend requests, acceptance/rejections, friends list.
* **Educational Guide for Mentorship**: See [DTO_GUIDE.md](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/DTO_GUIDE.md).

---

### 🚧 Phase 4: Service Layer Refactoring & Business Logic (Next Up)
* **Goal**: Transition business logic out of controllers into `@Service` classes with `@Transactional` boundaries.
* **Services to build**:
  * `UserService`: Password hashing, profile management, username uniqueness.
  * `ConversationService`: Atomic 1-on-1 conversation creation with junction records.
  * `MessageService`: Participant authorization check, message persistence, audit logging, WebSocket trigger hook.
  * `GroupService`: Group creation with automatic ADMIN assignment, member promotion/demotion.
  * `FriendService`: Friendship state machine (`PENDING` ➔ `ACCEPTED` / `REJECTED`).
* **Educational Guide for Mentorship**: See [SERVICE_LAYER_GUIDE.md](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/SERVICE_LAYER_GUIDE.md).

---

### 📋 Phase 5: Authentication & Security Architecture (Planned)
* **Goal**: Secure the REST API and real-time endpoints with modern authentication.
* **Key Tasks**:
  * Implement BCrypt password hashing (`BCryptPasswordEncoder`).
  * Integrate Spring Security filter chain with Stateless JWT Bearer tokens.
  * Create `JwtAuthenticationFilter` and `JwtTokenProvider`.
  * Protect mutating REST routes and configure public registration/login routes.
  * Secure WebSocket handshake headers with JWT verification.
* **Comprehensive Architectural Plan & Trade-Offs**: See [SECURITY_PLAN.md](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/SECURITY_PLAN.md).

---

### 📋 Phase 6: Real-Time Messaging via WebSockets & STOMP (Planned)
* **Goal**: Instant message delivery and event push without client polling.
* **Key Tasks**:
  * Configure `WebSocketMessageBrokerConfigurer` with `/ws` endpoint and SockJS fallback.
  * Define destination prefixes: `/app` for incoming messages, `/topic` for public/group broadcasts, `/queue` for user-specific direct messages.
  * Implement real-time events:
    * `NEW_MESSAGE`: Real-time chat stream.
    * `MESSAGE_EDIT`: In-place message edit updates.
    * `USER_TYPING`: Typing indicator events.
    * `PRESENCE`: Online / Offline user status.

---

### 📋 Phase 7: Front-End UI Integration & Testing (Planned)
* **Goal**: Connect the front-end user interface to `chat-middleware`.
* **Key Tasks**:
  * Integrate REST API endpoints with Axios / Fetch API client.
  * Integrate STOMP WebSocket client (`@stomp/stompjs` / `sockjs-client`).
  * End-to-end testing with mock users, active chat rooms, and real-time message exchange.

---

## 3. Quick Links to Project Documentation
* [Entity Architecture & Schema Report](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/ENTITY_ANALYSIS.md)
* [Service Layer Educational Guide](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/SERVICE_LAYER_GUIDE.md)
* [Security & Authentication Architecture Plan](file:///home/james/Documents/Mentorship/chat-room/chat-middleware/SECURITY_PLAN.md)
