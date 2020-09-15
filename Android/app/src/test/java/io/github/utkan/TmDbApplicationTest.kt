package io.github.utkan

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class TmDbApplicationTest {

    @Test
    fun `test onCreate`() {
        // given
        val loggerConfigurator: LoggerConfigurator = mock()
        val app = TmDbApplication()
        app.loggerConfigurator = loggerConfigurator

        // when
        app.onCreate()

        // then
        verify(loggerConfigurator).configure()
    }
}
