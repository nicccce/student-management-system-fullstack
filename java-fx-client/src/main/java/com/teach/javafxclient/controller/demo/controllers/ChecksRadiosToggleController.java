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

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.utils.ColorUtils;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeSolid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class ChecksRadiosToggleController implements Initializable {

	@FXML
	private MFXToggleButton customToggle;

	@FXML
	private MFXRectangleToggleNode r1;

	@FXML
	private MFXRectangleToggleNode r2;

	@FXML
	private MFXRectangleToggleNode r3;

	@FXML
	private void changeColors(ActionEvent event) {
		customToggle.setColors(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
		customToggle.setSelected(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		r1.setLabelLeadingIcon(FontAwesomeSolid.random(Color.BLACK, 16));
		r1.setLabelTrailingIcon(FontAwesomeSolid.random(Color.BLACK, 16));

		r2.setLabelLeadingIcon(FontAwesomeSolid.random(Color.BLACK, 16));
		r2.setLabelTrailingIcon(FontAwesomeSolid.random(Color.BLACK, 16));

		r3.setLabelLeadingIcon(FontAwesomeSolid.random(Color.BLACK, 16));
		r3.setLabelTrailingIcon(FontAwesomeSolid.random(Color.BLACK, 16));
	}
}
