/*
 * KOTLIN DIAGNOSTICS SPEC TEST (POSITIVE)
 *
 * SPEC VERSION: 0.1-100
 * PLACE: expressions, constant-literals, real-literals -> paragraph 5 -> sentence 1
 * NUMBER: 2
 * DESCRIPTION: A type checking of a real literal with an exponent mark.
 * HELPERS: checkType
 */

// TESTCASE NUMBER: 1
val value_1 = 0.0e0 checkType { check<Double>() }

// TESTCASE NUMBER: 2
val value_2 = 0.0e-0____0 checkType { check<Double>() }

// TESTCASE NUMBER: 3
val value_3 = 0.0E000 checkType { check<Double>() }

// TESTCASE NUMBER: 4
val value_4 = 0.0E+0_0_0_0 checkType { check<Double>() }

// TESTCASE NUMBER: 5
val value_5 = 0________________________________________0.0e+0 checkType { check<Double>() }

// TESTCASE NUMBER: 6
val value_6 = 000.0___0e0_0 checkType { check<Double>() }

// TESTCASE NUMBER: 7
val value_7 = 0000.000E-000 checkType { check<Double>() }

// TESTCASE NUMBER: 8
val value_8 = 1.0E+1 checkType { check<Double>() }

// TESTCASE NUMBER: 9
val value_9 = 2_________2.0_0e2_2 checkType { check<Double>() }

// TESTCASE NUMBER: 10
val value_10 = 33____3.000e-333 checkType { check<Double>() }

// TESTCASE NUMBER: 11
val value_11 = 4444.0000e4444 checkType { check<Double>() }

// TESTCASE NUMBER: 12
val value_12 = 55555.0E-5_5_5_5_5 checkType { check<Double>() }

// TESTCASE NUMBER: 13
val value_13 = 6_6_6_6_6_6.0____0e666666 checkType { check<Double>() }

// TESTCASE NUMBER: 14
val value_14 = 7777777.000E7777777 checkType { check<Double>() }

// TESTCASE NUMBER: 15
val value_15 = 88888888.0000e-88888888 checkType { check<Double>() }

// TESTCASE NUMBER: 16
val value_16 = 9_9_9_9_9_9_9_9_9.0E+9_9_9_9_9_9_9_9_9 checkType { check<Double>() }

// TESTCASE NUMBER: 17
val value_17 = 0_________0________0_______0______0_____0____0___0__0_0.1234567890E999999999 checkType { check<Double>() }

// TESTCASE NUMBER: 18
val value_18 = 123456789.23456789E+123456789 checkType { check<Double>() }

// TESTCASE NUMBER: 19
val value_19 = 2345678.345678e00000000001 checkType { check<Double>() }

// TESTCASE NUMBER: 20
val value_20 = 34567.4_5__6___7E-100000000000 checkType { check<Double>() }

// TESTCASE NUMBER: 21
val value_21 = 456.5________6e-0 checkType { check<Double>() }

// TESTCASE NUMBER: 22
val value_22 = 5.65e000000000000 checkType { check<Double>() }

// TESTCASE NUMBER: 23
val value_23 = 654.7654E+0__1_0 checkType { check<Double>() }

// TESTCASE NUMBER: 24
val value_24 = 76543.876543E1 checkType { check<Double>() }

// TESTCASE NUMBER: 25
val value_25 = 8765432.98765432e-2 checkType { check<Double>() }

// TESTCASE NUMBER: 26
val value_26 = 9_8765432_1.0987654321E-3 checkType { check<Double>() }

// TESTCASE NUMBER: 27
val value_27 = 0.111_1e4 checkType { check<Double>() }

// TESTCASE NUMBER: 28
val value_28 = 1.2_2222E-5 checkType { check<Double>() }

// TESTCASE NUMBER: 29
val value_29 = 9.33333e+6 checkType { check<Double>() }

// TESTCASE NUMBER: 30
val value_30 = 9.444444E7 checkType { check<Double>() }

// TESTCASE NUMBER: 31
val value_31 = 8.5555555e8 checkType { check<Double>() }

// TESTCASE NUMBER: 32
val value_32 = 2.66666666e3_0_8 checkType { check<Double>() }

// TESTCASE NUMBER: 33
val value_33 = 3.7________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________77777777E-308 checkType { check<Double>() }

// TESTCASE NUMBER: 34
val value_34 = 7.8888888888e+30____9 checkType { check<Double>() }

// TESTCASE NUMBER: 35
val value_35 = 6.99999999999e-309 checkType { check<Double>() }

// TESTCASE NUMBER: 36
val value_36 = 7.888888___888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888e+111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111 checkType { check<Double>() }

// TESTCASE NUMBER: 37
val value_37 = 0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000_0 checkType { check<Double>() }

// TESTCASE NUMBER: 38
val value_38 = 0.0_00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e-000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 checkType { check<Double>() }

// TESTCASE NUMBER: 39
val value_39 = 0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e+000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 checkType { check<Double>() }
