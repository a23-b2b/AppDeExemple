package ca.qc.bdeb.c5gm.appdeexemple

import androidx.lifecycle.viewmodel.viewModelFactory
import org.junit.Assert.*

import org.junit.Test

class JeuDeViewModelTest {

    private val viewModel = JeuDeViewModel()

    @Test
    fun changerJoueur() {
        assertEquals(0, viewModel.joueurCourant)
        viewModel.changerJoueur()
        assertEquals(1, viewModel.joueurCourant)
        viewModel.changerJoueur()
        assertEquals(0, viewModel.joueurCourant)
    }

    @Test
    fun getJoueurCourant() {
        assertEquals(0, viewModel.joueurCourant)
        viewModel.changerJoueur()
        assertEquals(1, viewModel.joueurCourant)
        viewModel.changerJoueur()
        assertEquals(0, viewModel.joueurCourant)
    }
}