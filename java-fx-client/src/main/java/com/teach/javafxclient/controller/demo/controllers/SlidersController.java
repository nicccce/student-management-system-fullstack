/*
 * Copyright (C) 2022 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.teach.javafxclient.controller.demo.controllers;

import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SlidersController implements Initializable {

	@FXML
	private MFXSlider customSlider;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customSlider.getRanges1().add(NumberRange.of(customSlider.getMin(), 33.0));
		customSlider.getRanges2().add(NumberRange.of(34.0, 66.0));
		customSlider.getRanges3().add(NumberRange.of(67.0, customSlider.getMax()));
	}
}
