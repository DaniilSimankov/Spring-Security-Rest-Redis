# Spring Boot Security REST with Redis.

Each authorized user was assigned a unique Token, which was blacklisted when Logout. The blacklist is stored in Redis for read speed.

Protection is provided by filters. Aspect programming elements are also present