# Java State Space Analysis

Implementation of Petrinet and State Space generation by BFS

##### Syntax specification
- Value type: 
    - **String**: use single quote (Ex: `'This is a string'`)
    - **Integer**: integer number literal (Ex: `1`, `69`, `-42`)
    - **Real**: floating number literal (Ex: `3.4`, `-03.13`, `.99`)
    - **Boolean**: boolean literal (Ex: `True`, `False`)
    - **Variable**: variables that contained value of 4 above data types (Ex: `n`, `money`, `numAccount`)
- Arithmetic operators: `+`, `-`, `*`, `/`, `%`
- Logic operators: `&&`, `!`, `||`, `^`, `isTrue`, `isFalse`
- Compare operators: `==`, `!=`, `>`, `<`, `>=`, `<=`
- String operators: 
    - **substr**: `'ferdon' 1 3 substr` => `'erd'`
    - **append**: `'fer' 'don' append` => `'ferdon'`
    - **isEmpty**: `'' isEmpty` => `True`
    - **trim**: `'  ferdon '` => `ferdon`
- Condition operators: 
    - **if**: `1 1 == 'ferdon' if` => `'ferdon'`
    - **ifelse**: `1 1 != 'abc' 'ferdon' ifelse` => `'ferdon'`
- Array operators: 
    - **[**: create empty array
    - **,** and **]**: add new element to array (add nothing if empty array)

##### Input Petrinet JSON
- **Expression**: array of each **Transition** to **output places** 
    ```
    Expression: [
        [
            [2, "[ s , n ]"]  # Transition 0 is connected with place 2 by out edge, fire token with value [s, n]
        ],
        [
            [1, "[ ]"]  # Transition 1 is connected with place 1 by out edge, fire token with value [] (unit token)
            [0, "[ a b + ]"]  # Transition 1 is connected with place 0 by out edge, fire token with value [a + b] (postfix)
        ]
    ]
    ```
    *Note*: spaces between expression is important for parsing
-   **Guards**: array of string which is contained checking condition at each **place**
    ```
        "Guards": [
            "a b >",  # Guard at place 0 in postfix (a > b)
            "1 1 + 2 ==" # Guard at place 1 in postfix (1 + 1 == 2)
        ],
    ```
-   ***Marking***: array of string contained the initial tokens information in each **place**
    ```
        "Markings": [
            "[ 'a' ], [ 'b' ], [ 'c' ]",
            "[ 1 ], [ 2 ], [ 3 ]",
            ""
          ]
    ```
-   ***T***: number of transitions
    ```
        "T": 4  # There are 4 transitions in petrinet
    ```
-   ***Variables***: array of each **Transition** to **input places**      
    ```
    Variables: [
        [
            [2, "s , n"]  # Transition 0 is connected with place 2 by input edge, input variables are s and n
        ],
        [
            [1, ""]  # Transition 1 is connected with place 1 by input edge, input variable is unit
            [0, "a , b"]  # Transition 1 is connected with place 0 by input edge, input variables are a and b
        ]
    ]
    ```
-   ***inPlace***: simplified version of ***Variable*** without specified the input edge's information
    ```
        "inPlace": [
            [
                2
            ], 
            [
                1,
                0
            ]
        ]
    ```
-   **outPlace**: simplified version of ***Expression*** without specified the output edge's information
-   ```
        "outPlace": [
            [
                2
            ], 
            [
                1,
                0
            ]
        ]
    ```
-   **placeToColor**: data type of **places**
    ```
        "placeToColor": {
            "0": "STRING",
            "1": "INT",
            "2": "STRING*INT"
        }
    ```