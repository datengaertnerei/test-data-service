import csv

import lxml.html as lh
import requests

csv_writer = None


def extract_row_values(values):
    cc_type = ''
    cc_bin = ''
    children = list(values[1])
    if 0 < len(children):
        cc_type = children[0].text
    children = list(values[2])
    if 0 < len(children):
        children = list(children[0])
        if 0 < len(children):
            cc_bin = children[0].text
    if len(cc_type) > 0 and len(cc_bin) > 0:
        csv_writer.writerow([cc_type, cc_bin])


if __name__ == '__main__':
    host = 'https://bincheck.org'
    path = '/germany'  # Create a handle, page, to handle the contents of the website
    page = requests.get(host + path)  # Store the contents of the website under doc
    csv_file = open('binlist.csv', 'w', newline='')
    csv_writer = csv.writer(csv_file, delimiter=';', quoting=csv.QUOTE_NONE)
    while len(path) > 0:
        doc = lh.fromstring(page.content)  # Parse data that are stored between <tr>..</tr> of HTML
        rows = doc.xpath('//tr')
        for row in rows:
            extract_row_values(row)

        next_page = doc.xpath('//a[@rel="next"]/@href')
        if len(next_page) > 0:
            path = next_page[0]
        else:
            path = ''
        page.close()
        page = requests.get(host + path)
