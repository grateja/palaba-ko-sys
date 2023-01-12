package com.csi.palabakosys.model

import android.util.Patterns

sealed class Rule(var message: String) : RuleInterface {

    class EMAIL(private val email: String?) : Rule("No a valid email format") {
        override fun isValid(input: Any?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(input.toString()).matches()
        }
    }

    object REQUIRED : Rule("Field is required") {
        override fun isValid(input: Any?): Boolean {
            return input != null && input != "" && input != "null"
        }
    }

    class MATCHED(private val matchedWith: String?) : Rule("Fields do not matched") {
        constructor(matchedWith: String?, defaultMessage: String) : this(matchedWith) {
            message = defaultMessage
        }

        override fun isValid(input: Any?): Boolean {
            return matchedWith == input
        }
    }

    class MIN(private val minimumValue: Float?) : Rule("Field must be greater than $minimumValue") {
        constructor(value: Float?, defaultMessage: String) : this(value) {
            message = defaultMessage
        }

        override fun isValid(input: Any?): Boolean {
            val _input = input.toString().toFloatOrNull()?:0f
            val _min = this.minimumValue?:0f
            return _input >= _min
        }
    }

    class TRUE(private val isIt: Boolean) : Rule("Invalid input") {
        constructor(isIt: Boolean, defaultMessage: String) : this(isIt) {
            message = defaultMessage
        }

        override fun isValid(input: Any?): Boolean {
            return isIt
        }
    }
}
