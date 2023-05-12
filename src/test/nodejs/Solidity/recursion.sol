pragma solidity ^0.8.17;

contract Recursion {
    function sum(uint32 x) public pure returns (uint32) {
        if(x > 0){
            return x + sum(x - 1);
        }
        return 0;
    }

    function prog(uint32 x) public pure returns (uint32) {
            return sum(x);
    }
}

