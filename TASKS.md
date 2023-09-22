## Tasks

1. Add spring module (branch: task-add-spring-webflux)
Rethink architecture (kafka module, ktor module)
Add spring boot
Add webflux endpoints
2. Add mongo support (branch: task-add-mongo)
Add lock in parallel processing in COR
Add optimistic and pessimistic locks in postgres/mongo
3. Refactor code (branch: task-refactor)
Rename all otus occurrences
Improve code throughout all modules
Remove ktor module. But in this case we need to move all logic to spring. Second, I may leave it here for history