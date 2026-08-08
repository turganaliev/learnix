package com.turganaliev.learning_management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


// NOTE: This test currently passes using an embedded H2 fallback in CI,
// since CI has no PostgreSQL service configured and application-dev.properties
// (with real DB credentials) is gitignored. This means it does NOT verify
// PostgreSQL-specific compatibility. TO DO: add a PostgreSQL service to the
// GitHub Actions workflow to close this gap.
@SpringBootTest
class LearningManagementApplicationTests {

	@Test
	void contextLoads() {
	}

}
