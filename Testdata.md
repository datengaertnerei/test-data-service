# Other test data generators

Depending on your requirements test data management can be quite complex. For synthetic data there are various test data generator packages available. I will not endorse any commercial products here, but there are of course quite a few for sale. Here are just links to open source solutions.

## Faker

Faker is the obvious choice for random data with a wide range of data types and localization support. Faker is ok as long as you do not need valid(!) data for certain purposes like postal addresses, IBANs for current accounts or credit card numbers. That is the only downside I know.

It is available for various different programming languages:
| Prog. Lang. | URL |
| --- | --- |
| Java | [https://github.com/DiUS/java-faker](https://github.com/DiUS/java-faker) |
| Python | [https://github.com/joke2k/faker](https://github.com/joke2k/faker) |
| C# | [https://github.com/bchavez/Bogus](https://github.com/bchavez/Bogus) |
| JavaScript | [https://github.com/marak/Faker.js/](https://github.com/marak/Faker.js/) |
| PHP | [https://github.com/fzaninotto/Faker](https://github.com/fzaninotto/Faker) |
| Go | [https://github.com/bxcodec/faker](https://github.com/bxcodec/faker) |
| Swift | [https://github.com/vadymmarkov/Fakery](https://github.com/vadymmarkov/Fakery) |
| Perl | [https://metacpan.org/pod/Data::Faker](https://metacpan.org/pod/Data::Faker) |
| Ruby | [https://github.com/faker-ruby/faker](https://github.com/faker-ruby/faker) |
| Rust | [https://github.com/cksac/fake-rs](https://github.com/cksac/fake-rs) |

## Other

Another simple [test data generator](https://github.com/presidentio/test-data-generator) is designed to insert data into a database directly.

The [FINRA Open Source Data Generator ](http://finraos.github.io/DataGenerator/) uses modeled state charts to generate large and complex datasets.

Online generators like [Mockaroo](https://www.mockaroo.com/) or [FillDB](http://filldb.info/) let you create random test data without even installing any software.

