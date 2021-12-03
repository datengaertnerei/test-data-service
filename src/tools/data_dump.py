import requests
import csv

if __name__ == '__main__':
    response = requests.get("http://localhost/api/v1/bundles?size=100")
    bundles = response.json()
    csv_file = open('imaginary_people.csv', 'w', newline='', encoding="utf-8")
    csv_writer = csv.writer(csv_file, delimiter=';', quoting=csv.QUOTE_NONNUMERIC)
    csv_writer.writerow(
        ['given_name', 'family_name', 'gender', 'birth_date', 'height', 'eye_color', 'email', 'tax_id',
         'profession', 'street_address', 'house_number', 'postal_code', 'address_locality', 'address_country',
         'mobile', 'landline', 'cc_number', 'cc_type', 'cc_cvc', 'cc_expiry', 'bank_name', 'bank_city',
         'bank_code', 'bank_bic', 'bank_iban'])
    for bundle in bundles:
        given_name = bundle['person']['givenName']
        family_name = bundle['person']['familyName']
        gender = bundle['person']['gender']
        birth_date = bundle['person']['birthDate']
        height = bundle['person']['height']
        eye_color = bundle['person']['eyecolor']
        email = bundle['person']['email']
        tax_id = bundle['person']['taxId']
        profession = bundle['person']['profession']
        street_address = bundle['person']['address']['streetAddress']
        house_number = bundle['person']['address']['houseNumber']
        postal_code = bundle['person']['address']['postalCode']
        address_locality = bundle['person']['address']['addressLocality']
        address_country = bundle['person']['address']['addressCountry']
        mobile = bundle['mobile']['phoneNumber']
        landline = bundle['landline']['phoneNumber']
        cc_number = bundle['creditCard']['number']
        cc_type = bundle['creditCard']['type']
        cc_cvc = bundle['creditCard']['cvc']
        cc_expiry = bundle['creditCard']['expiry']
        bank_name = bundle['bankAccount']['bank']['desc']
        bank_city = bundle['bankAccount']['bank']['city']
        bank_code = bundle['bankAccount']['bank']['bankCode']
        bank_bic = bundle['bankAccount']['bank']['bic']
        bank_iban = bundle['bankAccount']['iban']
        csv_writer.writerow(
            [given_name, family_name, gender, birth_date, height, eye_color, email, tax_id, profession, street_address,
             house_number, postal_code, address_locality, address_country, mobile, landline, cc_number, cc_type, cc_cvc,
             cc_expiry, bank_name, bank_city, bank_code, bank_bic, bank_iban])
