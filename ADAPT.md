# Adapt the service to another country

So you want to use this in your local context outside of Germany. I will guide you through the necessary steps.

## Resources for names

The random person generator uses text files with lists of male and female names as well as common surnames. They are located in the resources tree. You can just exchange the contents with something that fits your needs. If you need inspiration, you can take a look at [Faker resources](https://github.com/DiUS/java-faker/tree/master/src/main/resources).

## Resources for phone area codes

In the resources tree you have to exchange the ```prefixlist.csv``` file with the correct contents of your local phone network operator. 

## Resources for bank accounts and credit cards

Credit cards are easy. Just exchange the ```binlist.csv``` file in the resources tree with valid BINs of some of your local issuers. 

For bank accounts you have to be inside the SEPA area or at least in an IBAN country to use the existing code. For Germany you need to generate a random domestic bank account number first, then calculate a checksum and combine the result with the german domestic bank code. You need to get a list of valid bank codes from the german federal bank including the checksum algorithm used by the corresponding bank. I implemented just one of the several possible algorithms an use a matching subset of that list. This first part of creating a valid SEPA bank account with IBAN and BIC has to be reimplemented for your local environment. You have to change the class ```com.datengaertnerei.test.dataservice.bank.BankGenerator.java```.

A good start is the [Wikipedia IBAN page](https://en.wikipedia.org/wiki/International_Bank_Account_Number#IBAN_formats_by_country). If you successfully adapted the creation of the domestic part of your bank account, you can reuse the part for building an IBAN.

## Valid postal addresses from Open Street Map

I am using Open Street Map dumps to populate an embedded H2 database with valid german address data. The class ```com.datengaertnerei.test.dataservice.person.OsmPbfAddressImportUtil.java``` contains an embedded sink to filter by country code DE, that is included as a constant value. You can change that, if one country code is enough for you. It is easy to extend the code to use a list of country codes. Or you can use a tailored OSM dump.