# High-Concurency-Flash-Sale-Engine


  █ THRESHOLDS 

    http_req_failed{status:500}
    ✓ 'rate<0.01' rate=0.00%


  █ TOTAL RESULTS 

    checks_total.......: 32130  912.606558/s
    checks_succeeded...: 32.71% 10512 out of 32130
    checks_failed......: 67.28% 21618 out of 32130

    ✗ Status 200 (Mua thành công)
      ↳  0% — ✓ 57 / ✗ 10653
    ✗ Status 429 (Bị Chặn Spam)
      ↳  97% — ✓ 10455 / ✗ 255
    ✗ Status 400 (Báo Hết Hàng)
      ↳  0% — ✓ 0 / ✗ 10710

    HTTP
    http_req_duration..............: avg=2s    min=0s       med=1.91s max=7.59s p(90)=3.61s p(95)=3.89s
      { expected_response:true }...: avg=2.89s min=244.4ms  med=2.61s max=7.59s p(90)=4.65s p(95)=4.9s 
    http_req_failed................: 99.46% 10653 out of 10710
      { status:500 }...............: 0.00%  0 out of 0
    http_reqs......................: 10710  304.202186/s

    EXECUTION
    iteration_duration.............: avg=2.21s min=145.32ms med=2.11s max=7.73s p(90)=3.82s p(95)=4.08s
    iterations.....................: 10710  304.202186/s
    vus............................: 33     min=0              max=1000
    vus_max........................: 1000   min=996            max=1000

    NETWORK
    data_received..................: 2.9 MB 82 kB/s
    data_sent......................: 1.2 MB 35 kB/s




running (0m35.2s), 0000/1000 VUs, 10710 complete and 0 interrupted iterations
default ✓ [======================================] 0000/1000 VUs  35s

![alt text](image.png)