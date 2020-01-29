package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.dialog.FilterDialogController;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = FilterDialogControllerModule.class)
public interface FilterDialogControllerComponent {
    FilterDialogController getFilterDialogController();
}