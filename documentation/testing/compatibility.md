# Compatibility with testing frameworks

We tried to make the testing suite decoupled from any testing framework (JUnit4/JUnit5/Kotest and etc.) and want to try to keep it so.
`rib-base-test-activity` the only module that has direct dependency on JUnit4 and Espresso to provide convenient rule to run tests.
If you experience any incompatibility issues, feel free to create your own test helper classes based on our logic that suits your requirements.
You can share your experience in the issues section or propose a pull request to improve it.
