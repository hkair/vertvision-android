# Vertical_Jump

## Overview

🏀 An Android app to calculate your vertical jump by setting the take off time and landing time.

## Installation

### Download vertical_jump.apk in the repository and install into your android.

## Usage

### 1. Record Video in 8x slow motion, in widescreen mode.

### 2. Load video.

### 3. Set Take off Time
<img src="images/22140033_2232702583622906_935081714_o.png" width="263" height="512">

### 4. Set Landing Time
<img src="images/22139852_2232702590289572_673294951_o.png" width="263" height="512">

### 5. Calculate the Vertical
<img src="images/22163744_2232702613622903_1346424875_o.png" width="263" height="512">


## Comments

Make sure when are you setting the take off time, it is when the feet just leaves the floor.

And the landing time as when the feet just touches the floor.

This is vital for the kinematic equation calculation, so that it only considers the initial time and the final time and not during the acceleration and deceleration phase.

The videos are slowed down by 8 times its normal speed, so the calculation is calibrated to videos that are 8 times slower.

## Future Additions

* Addition of multiple slow motion speed.
* OpenCv implementation.
