# Changelog

## 1.0.0-alpha01 &rarr; 1.0.0-alpha02
#### UI-Fixes
- Fix "x days ago" text being calculated wrongly
- Fix dream item corners being shown wrongly in dreamfeed
- Several statistics fixes

#### Misc. Fixes
- Fix being able to import/export in preview mode. Now you can't anymore.

#### Misc. Changes
- Properly setup different buildTypes

## 1.0.0-alpha02 &rarr; 1.0.0-alpha03
#### UI-Fixes
- Changes app icon colors to orange tones
- Fixed showing "0 days ago" on dream item when it was edited that day.

#### Misx. Fixes
- Removed debug logger statements
- Fixed that when editing a dream, this would also alter the created timestamp instead of only the edited timestamp.

## 1.0.0-alpha03 &rarr; 1.0.0-alpha06
#### UI-Fixes
- Fixed a bug where the app would crash when the dreams tab would be selected on the Person/location detail screen

## 1.0.0-alpha06 &rarr; 1.0.0-alpha07
#### Misc. Fixes
- Fixed a big where deleting a dream/person would not delete the crossreferences of that object. This would result in errors importing the exported file.
