import Prime8 (primes)
import Criterion.Main


psum n = sum $ take n primes

main = defaultMain [
  bgroup "prime1"[ 
                 bench "1000"  $ nf psum 1000
               -- , bench "10"  $ nf psum 10
               -- , bench "100"  $ nf psum 100
               -- , bench "1000"  $ nf psum 10000
                , bench "10000"  $ nf psum 100000
                , bench "100000"  $ nf psum 1000000
               -- , bench "1000000"  $ nf psum 10000000
               ]
--  ,bgroup "prime2" (map (\n-> bench (show n) (nf psum n)) 
--                       (take 10 (iterate (*2) 100)))
  ]