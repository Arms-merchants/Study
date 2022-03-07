package com.arms.flowview.proxy;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
interface IRentHouse {
    // 带领租客看房
    void visitHouse();

    // 讨价还价
    void argueRent(int rent);

    // 签合同
    void signAgreement();
}
