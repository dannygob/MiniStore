package com.ministore.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.runBlocking // For simplicity in this example, using runBlocking. Consider TestCoroutineDispatcher for more complex tests.
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AuthRepositoryImplTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        mockAuth = mockk()
        authRepository = AuthRepositoryImpl(mockAuth)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll() // Clear mocks after each test
    }

    @Test
    fun `signIn with valid credentials returns success`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val mockFirebaseUser = mockk<FirebaseUser>()
        val mockAuthResult = mockk<AuthResult>()
        val mockTask = mockk<Task<AuthResult>>()

        every { mockAuthResult.user } returns mockFirebaseUser
        every { mockTask.isSuccessful } returns true
        every { mockTask.isComplete } returns true
        every { mockTask.exception } returns null
        every { mockTask.result } returns mockAuthResult // Crucial for .await() to work as expected with mocks

        // Mocking the Task<AuthResult> behavior for await()
        // This is a simplified way; real await() is an extension function.
        // For robust mocking of await(), you might need 'mockk-android-gms' or more elaborate Task mocking.
        // However, for this conceptual test, we'll mock the direct FirebaseAuth call.

        coEvery { mockAuth.signInWithEmailAndPassword(email, password) } returns mockAuthResult // Simulate await() success

        // Act
        val result = authRepository.signIn(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockFirebaseUser, result.getOrNull())
        coVerify(exactly = 1) { mockAuth.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun `signIn with invalid credentials returns failure`() = runBlocking {
        // Arrange
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val exception = RuntimeException("Authentication failed")

        coEvery { mockAuth.signInWithEmailAndPassword(email, password) } throws exception

        // Act
        val result = authRepository.signIn(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { mockAuth.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun `signUp with new valid credentials returns success`() = runBlocking {
        // Arrange
        val email = "newuser@example.com"
        val password = "newpassword123"
        val mockFirebaseUser = mockk<FirebaseUser>()
        val mockAuthResult = mockk<AuthResult>()

        every { mockAuthResult.user } returns mockFirebaseUser
        // Simulate await() success for createUserWithEmailAndPassword
        coEvery { mockAuth.createUserWithEmailAndPassword(email, password) } returns mockAuthResult

        // Act
        val result = authRepository.signUp(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockFirebaseUser, result.getOrNull())
        coVerify(exactly = 1) { mockAuth.createUserWithEmailAndPassword(email, password) }
    }

    @Test
    fun `signUp with existing email or invalid details returns failure`() = runBlocking {
        // Arrange
        val email = "existing@example.com"
        val password = "weakpassword"
        val exception = RuntimeException("Sign up failed (e.g., email already in use)")

        coEvery { mockAuth.createUserWithEmailAndPassword(email, password) } throws exception

        // Act
        val result = authRepository.signUp(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { mockAuth.createUserWithEmailAndPassword(email, password) }
    }

    // --- Mocks for Task to simulate await() behavior if needed for more complex scenarios ---
    // This is a more involved setup for mocking Tasks if coEvery { ... } returns mockTask doesn't suffice.
    // For now, coEvery { mockAuth.signInWithEmailAndPassword(...) } returns mockAuthResult is simpler and works for this case.
    /*
    private fun <T> mockTask(result: T?, exception: Exception? = null): Task<T> {
        val task = mockk<Task<T>>(relaxed = true)
        every { task.isComplete } returns true
        every { task.isSuccessful } returns (exception == null)
        every { task.result } returns result
        every { task.exception } returns exception
        return task
    }
    */
}
