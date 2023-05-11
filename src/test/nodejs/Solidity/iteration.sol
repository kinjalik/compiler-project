pragma solidity ^0.8.17;

contract Iteration {
    function sum(uint32 x) public pure returns (uint32) {
        uint32 result = x;
        while(x != 0){
            x = x - 1;
            result = result + x;
        }
        return result;
    }

    function prog(uint32 x) public pure returns (uint32) {
        return sum(x);
    }
}

