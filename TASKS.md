## Tasks

1. Add spring module
   - Add spring boot+
   - Rethink architecture
   - Add webflux endpoints
   - Add multi instance deploy to test app under high load
2. Add mongo support
   - Add lock in parallel processing in COR
   - Add optimistic and pessimistic locks in postgres/mongo
3. Refactor code
   - Rename all otus occurrences
   - Improve code throughout all modules
   - Remove ktor module. But in this case we need to move all logic to spring. Second, I may leave it here for history