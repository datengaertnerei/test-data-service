import csv

import lxml.html as lh
import requests

if __name__ == '__main__':
    host = 'https://www.bundesbank.de'
    path = '/de/aufgaben/unbarer-zahlungsverkehr/serviceangebot/bankleitzahlen'  # Create a handle, page, to handle the contents of the website
    page = requests.get(host + path)  # Store the contents of the website under doc
    doc = lh.fromstring(page.content)  # Parse data that are stored between <tr>..</tr> of HTML
    link_element = doc.xpath('//a[@title=\'Download – Bankleitzahlen\']')
    path = link_element[0].attrib['href']
    page.close()
    page = requests.get(host + path)
    doc = lh.fromstring(page.content)  # Parse data that are stored between <tr>..</tr> of HTML
    link_element = doc.xpath('//a[contains(@href,\'blz-aktuell-txt-data.txt\')]')
    path = link_element[0].attrib['href']
    page.close()
    page = requests.get(host + path)
    table = page.text
    csvfile = open('banklist.csv', 'w', newline='', encoding="utf-8")
    csvwriter = csv.writer(csvfile, delimiter=';', quoting=csv.QUOTE_NONE)
    csvwriter.writerow(['Bankleitzahl', 'PLZ', 'Ort', 'Kurzbezeichnung', 'BIC', 'Prüfzifferberechnungsmethode'])
    lastbic = ''
    for line in table.splitlines():
        blz = line[:8]
        name = line[9:67].strip()
        zip = line[67:72]
        city = line[72:107].strip()
        desc = line[107:134].strip()
        bic = line[139:150].strip()
        if bic == '':
            bic = lastbic
        else:
            lastbic = bic
        check_method = line[150:152]
        if check_method == '00':
            csvwriter.writerow([blz, zip, city, desc, bic, check_method])
